package com.theapache64.stackzy.data.repo

import com.theapache64.stackzy.data.local.AnalysisReport
import com.theapache64.stackzy.data.local.Platform
import com.theapache64.stackzy.data.remote.Library
import java.io.File
import javax.inject.Inject

class ApkAnalyzerRepo @Inject constructor() {

    companion object {
        private val PHONEGAP_FILE_PATH_REGEX = "temp/smali(?:_classes\\d+)?/com(?:/adobe)?/phonegap".toRegex()
        private val FLUTTER_FILE_PATH_REGEX = "smali/io/flutter/embedding/engine/FlutterJNI.smali".toRegex()

        private const val DIR_REGEX_FORMAT = "smali(_classes\\d+)?\\/%s"
        private val APP_LABEL_MANIFEST_REGEX = "android:label=\"(.+?)\"".toRegex()
    }

    fun analyze(
        decompiledDir: File,
        allLibraries: List<Library>
    ): AnalysisReport {
        val platform = getPlatform(decompiledDir)
        return AnalysisReport(
            appName = getAppName(decompiledDir),
            platform = platform,
            libraries = getLibraries(platform, decompiledDir, allLibraries)
        )
    }

    fun getLibraries(
        platform: Platform,
        decompiledDir: File,
        allLibraries: List<Library>
    ): Map<String, List<Library>> {
        return when (platform) {
            is Platform.NativeJava,
            is Platform.NativeKotlin -> {

                // Get all used libraries
                var appLibraries = getAppLibraries(decompiledDir, allLibraries).toMutableList()
                appLibraries = mergeDep(appLibraries, "okhttp3", "retrofit2")

                // Now let's categories it
                val libWithCats = mutableMapOf<String, MutableList<Library>>()
                for (appLib in appLibraries) {
                    val catList = libWithCats.getOrPut(appLib.category) {
                        mutableListOf()
                    }
                    catList.add(appLib)
                }
                println("Cats: $libWithCats")
                return libWithCats
            }
            else -> {
                // TODO : Support other platforms
                mapOf()
            }
        }
    }

    private fun mergeDep(
        appLibraries: MutableList<Library>,
        libToRemove: String,
        libToRemoveFrom: String
    ): MutableList<Library> {
        val hasDepLib = appLibraries.find { it.packageName.toLowerCase() == libToRemoveFrom } != null
        if (hasDepLib) {
            // remove that lib
            val library = appLibraries.find { it.packageName == libToRemove }
            if (library != null) {
                appLibraries.removeIf { it.id == library.id }
            }
        }

        return appLibraries
    }

    fun getAppName(decompiledDir: File): String {
        // Get label key from AndroidManifest.xml
        val label = getAppNameLabel(decompiledDir)
        require(label != null) { "Failed to get label" }
        val appName = if (label.contains("@string/")) {
            getStringXmlValue(decompiledDir, label)
        } else {
            label
        }
        require(appName != null) { "Failed to get app name" }
        return appName
    }

    fun getStringXmlValue(decompiledDir: File, labelKey: String): String? {
        val stringXmlFile = File("${decompiledDir.absolutePath}/res/values/strings.xml")
        val stringXmlContent = stringXmlFile.readText()
        val stringKey = labelKey.replace("@string/", "")
        val regEx = "<string name=\"$stringKey\">(.+?)</string>".toRegex()
        return regEx.find(stringXmlContent)?.groups?.get(1)?.value
    }

    fun getAppNameLabel(decompiledDir: File): String? {
        val manifestFile = File("${decompiledDir.absolutePath}/AndroidManifest.xml")
        val manifestContent = manifestFile.readText()
        val match = APP_LABEL_MANIFEST_REGEX.find(manifestContent)
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
            else -> Platform.NativeJava()
        }
    }

    private fun isWrittenKotlin(decompiledDir: File): Boolean {
        return File("${decompiledDir.absolutePath}/kotlin").exists()
    }

    private fun isFlutter(decompiledDir: File): Boolean {
        return decompiledDir.walk()
            .find {
                it.name == "libflutter.so" ||
                        FLUTTER_FILE_PATH_REGEX.find(it.absolutePath) != null
            } != null
    }

    private fun isReactNative(decompiledDir: File): Boolean {
        return getAssetsDir(decompiledDir).listFiles()?.find { it.name == "index.android.bundle" } != null
    }

    private fun isXamarin(decompiledDir: File): Boolean {
        return decompiledDir.walk().find {
            it.name == "libxamarin-app.so" || it.name == "libmonodroid.so"
        } != null
    }

    private fun isCordova(decompiledDir: File): Boolean {
        val assetsDir = getAssetsDir(decompiledDir)
        val hasWWW = assetsDir.listFiles()?.find {
            it.name == "www"
        } != null

        if (hasWWW) {
            return File("${assetsDir.absolutePath}/www/cordova.js").exists()
        }

        return false
    }

    private fun isPhoneGap(decompiledDir: File): Boolean {
        val hasWWW = getAssetsDir(decompiledDir).listFiles()?.find {
            it.name == "www"
        } != null

        if (hasWWW) {
            return File("${decompiledDir.absolutePath}/smali/com/adobe/phonegap/").exists() || decompiledDir.walk()
                .find { file ->
                    val filePath = file.absolutePath
                    isPhoneGapDirectory(filePath)
                } != null
        }
        return false
    }

    private fun getAssetsDir(decompiledDir: File): File {
        return File("${decompiledDir.absolutePath}/assets/")
    }

    private fun isPhoneGapDirectory(filePath: String) = PHONEGAP_FILE_PATH_REGEX.find(filePath) != null

    fun getAppLibraries(
        decompiledDir: File,
        allLibraries: List<Library>
    ): MutableSet<Library> {
        val appLibs = mutableSetOf<Library>()
        decompiledDir.walk().forEach { file ->
            if (file.isDirectory) {
                for (remoteLib in allLibraries) {
                    val packageAsPath = remoteLib.packageName.replace(".", "/")
                    val dirRegEx = getDirRegExFormat(packageAsPath)
                    if (isMatch(dirRegEx, file.absolutePath)) {
                        appLibs.add(remoteLib)
                        break
                    }
                }
            }
        }
        return appLibs
    }

    private fun isMatch(dirRegEx: Regex, absolutePath: String): Boolean {
        return dirRegEx.find(absolutePath) != null
    }

    private fun getDirRegExFormat(packageAsPath: String): Regex {
        return String.format(DIR_REGEX_FORMAT, packageAsPath).toRegex()
    }

}