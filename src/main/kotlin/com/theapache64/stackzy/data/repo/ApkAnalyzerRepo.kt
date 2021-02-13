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

        private val APP_LABEL_MANIFEST_REGEX = "android:label=\"(.+?)\"".toRegex()
    }

    fun analyze(decompiledDir: File): AnalysisReport {
        val platform = getPlatform(decompiledDir)
        return AnalysisReport(
            appName = getAppName(decompiledDir),
            platform = platform,
            libraries = getLibraries(decompiledDir)
        )
    }

    private fun getLibraries(decompiledDir: File): Map<String, List<Library>> {
        return mapOf()
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


}