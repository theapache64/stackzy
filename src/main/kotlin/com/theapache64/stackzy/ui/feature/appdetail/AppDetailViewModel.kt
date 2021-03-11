package com.theapache64.stackzy.ui.feature.appdetail

import com.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.local.AnalysisReport
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.data.remote.UntrackedLibrary
import com.theapache64.stackzy.data.repo.*
import com.theapache64.stackzy.util.Either
import com.theapache64.stackzy.util.R
import com.theapache64.stackzy.util.calladapter.flow.Resource
import com.toxicbakery.logging.Arbor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.io.File
import java.net.URI
import javax.inject.Inject
import kotlin.math.roundToInt


class AppDetailViewModel @Inject constructor(
    private val adbRepo: AdbRepo,
    private val apkToolRepo: ApkToolRepo,
    private val apkAnalyzerRepo: ApkAnalyzerRepo,
    private val librariesRepo: LibrariesRepo,
    private val untrackedLibsRepo: UntrackedLibsRepo
) {

    companion object {
        val TABS = mutableListOf(
            "Libraries",
            "Details"
        )
    }

    private var decompileJob: Job? = null
    private val _fatalError = MutableStateFlow<String?>(null)
    val fatalError: StateFlow<String?> = _fatalError

    private val _analysisReport = MutableStateFlow<AnalysisReport?>(null)
    val analysisReport: StateFlow<AnalysisReport?> = _analysisReport

    private val _loadingMessage = MutableStateFlow<String?>(null)
    val loadingMessage: StateFlow<String?> = _loadingMessage

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex

    fun init(
        source: Either<AndroidDevice, Account>,
        androidApp: AndroidApp,
    ) {
        startDecompile(source, androidApp)
    }

    private fun startDecompile(
        source: Either<AndroidDevice, Account>,
        androidApp: AndroidApp
    ) {

        decompileJob = GlobalScope.launch {
            try {
                when(source){
                    is Either.Left -> {
                        val androidDevice = source.value
                        _loadingMessage.value = R.string.app_detail_loading_fetching_apk

                        // First get APK path
                        val apkRemotePath = adbRepo.getApkPath(androidDevice, androidApp)
                        if (apkRemotePath != null) {

                            val apkFile = kotlin.io.path.createTempFile(
                                suffix = ".apk"
                            ).toFile()

                            adbRepo.pullFile(
                                androidDevice,
                                apkRemotePath,
                                apkFile
                            ).distinctUntilChanged()
                                .catch {
                                    _fatalError.value = it.message ?: "Something went wrong while pulling APK"
                                }
                                .collect { downloadPercentage ->
                                    _loadingMessage.value = "Pulling APK $downloadPercentage% ..."

                                    if (downloadPercentage == 100) {
                                        // Give some time to APK to prepare for decompile
                                        _loadingMessage.value = "Preparing APK for decompiling..."
                                        delay(2000)
                                        onApkPulled(androidApp, apkFile)
                                    }

                                }
                        } else {
                            _fatalError.value = R.string.app_detail_error_apk_remote_path
                        }
                    }
                    is Either.Right -> TODO()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _fatalError.value = e.message
            }
        }
    }

    fun onBackPressed() {
        decompileJob?.cancel()
        Arbor.d("Cancelled decompile job")
        decompileJob = null
    }

    private suspend fun onApkPulled(androidApp: AndroidApp, apkFile: File) {
        // Now let's decompile
        _loadingMessage.value = R.string.app_detail_loading_decompiling
        val decompiledDir = apkToolRepo.decompile(apkFile)
        // val decompiledDir = File("build/topcorn_decompiled")

        // Analyse
        _loadingMessage.value = R.string.app_detail_loading_analysing
        val allLibraries = librariesRepo.getCachedLibraries()
        require(allLibraries != null) { "Cached libraries are null" }

        // Report
        val report = apkAnalyzerRepo.analyze(
            androidApp.appPackage.name,
            apkFile,
            decompiledDir,
            allLibraries
        )

        // Delete decompiled dir
        _loadingMessage.value = "Hold on please..."
        decompiledDir.deleteRecursively()
        apkFile.delete()

        trackUntrackedLibs(report)
        _analysisReport.value = report
        _loadingMessage.value = null
    }

    /**
     * TODO: ONLY FOR DEBUG PURPOSE
     */
    private suspend fun trackUntrackedLibs(report: AnalysisReport) {

        if (true) {
            return
        }

        if (report.untrackedLibraries.isNotEmpty()) {
            // Sync untracked libs
            untrackedLibsRepo.getUntrackedLibs()
                .collect { remoteUntrackedLibsResp ->
                    when (remoteUntrackedLibsResp) {
                        is Resource.Loading -> {
                            _loadingMessage.value = "Loading untracked libs..."
                        }
                        is Resource.Success -> {

                            // remove already listed libs and app packages
                            val newUntrackedLibs =
                                report.untrackedLibraries.filter { localUntrackedLib ->
                                    remoteUntrackedLibsResp.data.find { it.packageNames == localUntrackedLib } == null &&
                                            localUntrackedLib.startsWith(report.packageName).not()
                                }.map { UntrackedLibrary(it) }


                            val totalLibsToSync = newUntrackedLibs.size
                            var syncedLibs = 0
                            for (ut in newUntrackedLibs) {
                                untrackedLibsRepo.add(ut)
                                    .collect {
                                        when (it) {
                                            is Resource.Loading -> {
                                                val percentage = (syncedLibs.toFloat() / totalLibsToSync) * 100
                                                _loadingMessage.value =
                                                    "Adding ${ut.packageNames} to untracked libs... ${percentage.roundToInt()}%"
                                            }
                                            is Resource.Success -> {
                                                println("Done!! -> ${ut.packageNames}")
                                                syncedLibs++
                                            }

                                            is Resource.Error -> {
                                                println("Failed to sync: ${it.errorData}")
                                            }
                                        }
                                    }
                            }
                        }
                        is Resource.Error -> {

                        }
                    }
                }
        }
    }

    fun onTabClicked(index: Int) {
        _selectedTabIndex.value = index
    }

    fun onPlayStoreIconClicked() {
        _analysisReport.value?.let { report ->
            val playStoreUrl = URI("https://play.google.com/store/apps/details?id=${report.packageName}")
            Desktop.getDesktop().browse(playStoreUrl)
        }
    }
}