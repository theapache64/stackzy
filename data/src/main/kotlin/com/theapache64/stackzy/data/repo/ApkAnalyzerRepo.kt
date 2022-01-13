package com.theapache64.stackzy.data.repo

import com.theapache64.stackzy.data.local.AnalysisReport
import com.theapache64.stackzy.data.local.GradleInfo
import com.theapache64.stackzy.data.local.LibResult
import com.theapache64.stackzy.data.local.Platform
import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.data.util.AndroidVersionIdentifier
import com.theapache64.stackzy.data.util.StringUtils
import com.theapache64.stackzy.data.util.sizeInMb
import com.toxicbakery.logging.Arbor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.w3c.dom.Element
import java.io.File
import java.nio.file.Files.walk
import java.nio.file.Path
import java.util.*
import javax.inject.Inject
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.io.path.Path
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.io.path.readText

class ApkAnalyzerRepo @Inject constructor() {


    companion object {
        private val PHONEGAP_FILE_PATH_REGEX by lazy { "temp/smali(?:_classes\\d+)?/com(?:/adobe)?/phonegap".toRegex() }
        private val FLUTTER_FILE_PATH_REGEX by lazy { "io/flutter/embedding/engine/FlutterJNI\\.java".toRegex() }
        private val APP_LABEL_MANIFEST_REGEX by lazy { "<application.+?label=\"(.+?)\"".toRegex() }
        private val USER_PERMISSION_REGEX by lazy { "<uses-permission (?:android:)?name=\"(?<permission>.+?)\"/>".toRegex() }
        private val PACKAGE_NAME_REGEX by lazy { "package=\"(.+?)\"".toRegex() }
        private val IMPORT_REGEX by lazy { "import (.+)".toRegex() }
    }

    /**
     * To get final report
     */
    suspend fun analyze(
        packageName: String,
        apkFile: File,
        decompiledDir: File,
        allLibraries: List<Library>
    ): AnalysisReport = withContext(Dispatchers.IO) {
        val platform = getPlatform(decompiledDir)
        val libResult = getLibraries(platform, decompiledDir, allLibraries)
        AnalysisReport(
            appName = getAppName(decompiledDir) ?: packageName,
            packageName = packageName,
            platform = platform,
            appLibs = libResult.appLibs.sortedBy { it.category == Library.CATEGORY_OTHER },
            transitiveLibs = libResult.transitiveDeps.sortedBy { it.category == Library.CATEGORY_OTHER },
            untrackedLibraries = libResult.untrackedLibs,
            apkSizeInMb = "%.2f".format(Locale.US, apkFile.sizeInMb).toFloat(),
            assetsDir = getAssetsDir(decompiledDir).takeIf { it.exists() },
            permissions = getPermissions(decompiledDir),
            gradleInfo = getGradleInfo(decompiledDir)
        )
    }


    /**
     * To get versionCode, versionName, minSdk and targetSdk from YAML file generated by apk-tool.
     */
    fun getGradleInfo(decompiledDir: File): GradleInfo {
        val manifestFileContent = getManifestFile(decompiledDir).toFile()
        val manifestDoc = DocumentBuilderFactory.newInstance().apply {
            setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)
        }.newDocumentBuilder().parse(manifestFileContent)
        val versionCode = manifestDoc.documentElement.getAttribute("android:versionCode").toInt()
        val versionName = manifestDoc.documentElement.getAttribute("android:versionName")
        val minSdk = (manifestDoc.getElementsByTagName("uses-sdk").item(0) as Element)
            .getAttribute("android:minSdkVersion").toInt()
        val targetSdk = (manifestDoc.getElementsByTagName("uses-sdk").item(0) as Element)
            .getAttribute("android:targetSdkVersion").toInt()

        // Building gradle info
        return GradleInfo(
            versionCode = versionCode,
            versionName = versionName,
            minSdk = minSdk.let {
                val androidVersionName = AndroidVersionIdentifier.getVersion(it)
                GradleInfo.Sdk(it, androidVersionName)
            },
            targetSdk = targetSdk.let {
                val androidVersionName = AndroidVersionIdentifier.getVersion(it)
                GradleInfo.Sdk(it, androidVersionName)
            }
        )
    }

    /**
     * To get permissions used inside decompiled dir.
     */
    fun getPermissions(decompiledDir: File): List<String> {
        val manifestFile = Path(decompiledDir.absolutePath) / "resources" / "AndroidManifest.xml"
        return getPermissionsFromManifestFile(manifestFile)
    }

    fun getPermissionsFromManifestFile(manifestFile: Path): List<String> {
        val permissions = mutableListOf<String>()
        val manifestRead = manifestFile.readText()
        var matchResult = USER_PERMISSION_REGEX.find(manifestRead)
        while (matchResult != null) {
            permissions.add(matchResult.groupValues[1])
            matchResult = matchResult.next()
        }
        return permissions
    }

    /**
     * To get libraries used in the given decompiled app.
     *
     * Returns (untrackedLibs, usedLibs) in a Pair
     */
    fun getLibraries(
        platform: Platform,
        decompiledDir: File,
        allRemoteLibraries: List<Library>
    ): LibResult {
        return when (platform) {
            is Platform.NativeJava,
            is Platform.NativeKotlin -> {

                // Get all used libraries
                val libResult = getLibResult(decompiledDir, allRemoteLibraries)
                libResult.appLibs = mergeDep(libResult.appLibs)
                libResult.transitiveDeps = mergeDep(libResult.transitiveDeps)
                return libResult
            }
            else -> {
                // TODO : Support other platforms
                LibResult(
                    untrackedLibs = setOf(),
                    appLibs = setOf(),
                    transitiveDeps = setOf()
                )
            }
        }
    }

    /**
     * To merge dependencies.
     */
    private fun mergeDep(
        _appLibs: Set<Library>
    ): MutableSet<Library> {
        val appLibs = _appLibs.toMutableSet()
        val mergePairs = _appLibs
            .filter { it.replacementPackage != null }
            .map {
                Pair(it.replacementPackage, it.packageName)
            }
        for ((libToRemove, replacement) in mergePairs) {
            val hasDepLib = appLibs.find { it.packageName.lowercase(Locale.getDefault()) == replacement } != null
            if (hasDepLib) {
                // remove that lib
                val library = appLibs.find { it.packageName == libToRemove }
                if (library != null) {
                    appLibs.removeIf { it.id == library.id }
                }
            }
        }

        return appLibs
    }

    /**
     * To get app name from decompiled directory
     */
    fun getAppName(decompiledDir: File): String? {
        // Get label key from AndroidManifest.xml
        val label = getAppNameLabel(decompiledDir)
        if (label == null) {
            Arbor.w("Could not retrieve app name label")
            return null
        }
        var appName = if (label.contains("@string/")) {
            getStringXmlValue(decompiledDir, label)
        } else {
            label
        }
        if (appName == null || appName.startsWith("@string/")) {
            Arbor.w("Could not retrieve app name")
            return null
        }
        appName = StringUtils.removeApostrophe(appName)
        return appName
    }

    /**
     * To get the appropriate value for the given labelKey from string.xml in the decompiledDir.
     *
     * Returns null if not found
     */
    fun getStringXmlValue(decompiledDir: File, labelKey: String): String? {
        val stringXmlFile = Path(decompiledDir.absolutePath) / "resources" / "res" / "values" / "strings.xml"
        val stringXmlContent = stringXmlFile.readText()
        val stringKey = labelKey.replace("@string/", "")
        val regEx = "<string name=\"$stringKey\">(.+?)</string>".toRegex()
        Arbor.d("@string regEx : '$regEx'")
        val value = regEx.find(stringXmlContent)?.groups?.get(1)?.value
        return if (value != null) {
            if (value.startsWith("@string/")) {
                // pointing to another string res, so go recursive
                getStringXmlValue(decompiledDir, value)
            } else {
                value
            }
        } else {
            null
        }
    }

    /**
     * To get `label`'s value from AndroidManifest.xml
     */
    fun getAppNameLabel(decompiledDir: File): String? {
        val manifestFileContent = getManifestFile(decompiledDir).readText()
        val match = APP_LABEL_MANIFEST_REGEX.find(manifestFileContent)
        return if (match != null && match.groupValues.isNotEmpty()) {
            match.groups[1]?.value
        } else {
            null
        }
    }

    /**
     * To get platform from given decompiled APK directory
     */
    fun getPlatform(decompiledDir: File): Platform {
        return when {
            isPhoneGap(decompiledDir) -> Platform.PhoneGap()
            isCordova(decompiledDir) -> Platform.Cordova()
            isXamarin(decompiledDir) -> Platform.Xamarin()
            isReactNative(decompiledDir) -> Platform.ReactNative()
            isFlutter(decompiledDir) -> Platform.Flutter()
            isWrittenKotlin(decompiledDir) -> Platform.NativeKotlin()
            isUnity(decompiledDir) -> Platform.Unity()
            else -> Platform.NativeJava()
        }
    }

    private fun isUnity(decompiledDir: File): Boolean {
        return decompiledDir.resolve("resources").walk().find {
            it.name == "unity default resources"
        } != null
    }

    private fun isWrittenKotlin(decompiledDir: File): Boolean {
        // TODO: Check if app package files have kotlin package
        return (Path(decompiledDir.absolutePath) / "resources" / "kotlin").exists()
    }

    private fun isFlutter(decompiledDir: File): Boolean {
        return decompiledDir.resolve("sources").walk()
            .find {
                FLUTTER_FILE_PATH_REGEX.find(it.absolutePath) != null
            } != null
    }

    private fun isReactNative(decompiledDir: File): Boolean {
        return getAssetsDir(decompiledDir).listFiles()?.find { it.name == "index.android.bundle" } != null
    }

    private fun isXamarin(decompiledDir: File): Boolean {
        return File(decompiledDir.absolutePath ).resolve("resources").walk().find {
            it.name == "libxamarin-app.so" || it.name == "libmonodroid.so" || it.name == "Xamarin.Forms.Core.dll"
        } != null
    }

    private fun isCordova(decompiledDir: File): Boolean {
        val assetsDir = getAssetsDir(decompiledDir)
        val hasWWW = assetsDir.listFiles()?.find {
            it.name == "www"
        } != null

        if (hasWWW) {
            return (Path(assetsDir.absolutePath) / "www" / "cordova.js").exists()
        }

        return false
    }

    private fun isPhoneGap(decompiledDir: File): Boolean {
        val hasWWW = getAssetsDir(decompiledDir).listFiles()?.find {
            it.name == "www"
        } != null

        if (hasWWW) {
            return (Path(decompiledDir.absolutePath) / "smali" / "com" / "adobe" / "phonegap").exists() || decompiledDir.walk()
                .find { file ->
                    val filePath = file.absolutePath
                    isPhoneGapDirectory(filePath)
                } != null
        }
        return false
    }

    /**
     * To get asset directory from the given decompiledDir
     */
    private fun getAssetsDir(decompiledDir: File): File {
        return (Path(decompiledDir.absolutePath) / "resources" / "assets").toFile()
    }

    private fun isPhoneGapDirectory(filePath: String) = PHONEGAP_FILE_PATH_REGEX.find(filePath) != null

    /**
     * To get libraries used in the given decompiledDir (native app)
     */
    fun getLibResult(
        decompiledDir: File,
        remoteLibs: List<Library>
    ): LibResult {
        val appLibs = getAppLibs(decompiledDir, remoteLibs)
        val allUsedLibs = getAllUsedLibs(decompiledDir, remoteLibs)
        val transLibs = allUsedLibs - appLibs

        return LibResult(
            untrackedLibs = setOf(),// TODO:
            appLibs = appLibs,
            transitiveDeps = transLibs
        )
    }

    private fun getAllUsedLibs(decompiledDir: File, remoteLibs: List<Library>): Set<Library> {
        val allUsedLibs = mutableSetOf<Library>()
        (decompiledDir.toPath() / "sources").toFile().walk().forEach { file ->
            if (file.isDirectory) {
                for (remoteLib in remoteLibs) {
                    if (file.absolutePath.contains(remoteLib.packageName.replace(".", File.separator))) {
                        allUsedLibs.add(remoteLib)
                        break
                    }
                }
            }
        }
        return allUsedLibs
    }

    private fun getAppLibs(decompiledDir: File, remoteLibs: List<Library>): Set<Library> {
        val appLibs = mutableSetOf<Library>()
        val manifestPackageName = getManifestPackageName(decompiledDir)
        val allImports = getAllImports(decompiledDir, manifestPackageName)
        for (importStatement in allImports) {
            for (remoteLib in remoteLibs) {
                if (importStatement.contains(remoteLib.packageName)) {
                    appLibs.add(remoteLib)
                    break
                }
            }
        }
        return appLibs
    }


    private fun getAllImports(decompiledDir: File, manifestPackageName: String): Set<String> {
        val projectDir = decompiledDir.toPath() / "sources" / manifestPackageName.replace(".", File.separator)
        val allJavaFiles = projectDir.toFile().walk().filter { it.extension == "java" }
        val allImports = mutableSetOf<String>()
        for (javaFile in allJavaFiles) {
            javaFile.readLines().forEach { javaLine ->
                if (javaLine.startsWith("import ")) {
                    val match = IMPORT_REGEX.find(javaLine)
                    if (match != null) {
                        allImports.add(match.groupValues[1])
                    }
                }
            }
        }
        return allImports
    }

    private fun getManifestPackageName(decompiledDir: File): String {
        val manifestFileContent = getManifestFile(decompiledDir).readText()
        return PACKAGE_NAME_REGEX.find(manifestFileContent)?.groupValues?.get(1)
            ?: error("Couldn't find package name from manifest file")
    }

    private fun getManifestFile(decompiledDir: File) =
        (decompiledDir.toPath() / "resources" / "AndroidManifest.xml")

}


