package com.theapache64.stackzy.ui.feature.appdetail

import com.theapache64.stackzy.data.local.AnalysisReport
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.data.repo.AdbRepo
import com.theapache64.stackzy.data.repo.ApkAnalyzerRepo
import com.theapache64.stackzy.data.repo.ApkToolRepo
import com.theapache64.stackzy.data.repo.LibrariesRepo
import com.theapache64.stackzy.util.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


class AppDetailViewModel @Inject constructor(
    private val adbRepo: AdbRepo,
    private val apkToolRepo: ApkToolRepo,
    private val apkAnalyzerRepo: ApkAnalyzerRepo,
    private val librariesRepo: LibrariesRepo
) {


    private val _fatalError = MutableStateFlow<String?>(null)
    val fatalError: StateFlow<String?> = _fatalError

    private val _analysisReport = MutableStateFlow<AnalysisReport?>(null)
    val analysisReport: StateFlow<AnalysisReport?> = _analysisReport

    private val _loadingMessage = MutableStateFlow<String?>(null)
    val loadingMessage: StateFlow<String?> = _loadingMessage

    fun init(
        androidDevice: AndroidDevice,
        androidApp: AndroidApp,
    ) {
        GlobalScope.launch {

            _loadingMessage.value = R.string.app_detail_loading_fetching_apk

            // First get APK path
            val apkRemotePath = adbRepo.getApkPath(androidDevice, androidApp)
            if (apkRemotePath != null) {

                val destinationFile = kotlin.io.path.createTempFile(
                    suffix = ".apk"
                ).toFile()

                println("Path is ${destinationFile.absolutePath}")

                adbRepo.pullFile(
                    androidDevice,
                    apkRemotePath,
                    destinationFile
                ).distinctUntilChanged()
                    .collect { downloadPercentage ->
                        _loadingMessage.value = "Pulling APK $downloadPercentage% ..."
                        try {
                            if (downloadPercentage == 100) {
                                // Now let's decompile
                                _loadingMessage.value = R.string.app_detail_loading_decompiling
                                val decompiledDir = apkToolRepo.decompile(destinationFile)

                                // Analyse
                                _loadingMessage.value = R.string.app_detail_loading_analysing
                                val allLibraries = librariesRepo.getCachedLibraries()
                                require(allLibraries != null) { "Cached libraries are null" }

                                // Report
                                val report = apkAnalyzerRepo.analyze(decompiledDir, allLibraries)
                                _analysisReport.value = report
                                _loadingMessage.value = null
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            _fatalError.value = e.message
                        }
                    }
            } else {
                _fatalError.value = R.string.app_detail_error_apk_remote_path
            }
        }
    }
}