package com.theapache64.stackzy.ui.feature.appdetail

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
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppDetailViewModel @Inject constructor(
    private val androidDevice: AndroidDevice,
    private val androidApp: AndroidApp,
    private val adbRepo: AdbRepo,
    private val apkToolRepo: ApkToolRepo,
    private val apkAnalyzerRepo: ApkAnalyzerRepo,
    private val librariesRepo: LibrariesRepo
) {


    private val _fatalError = MutableStateFlow<String?>(null)
    val fatalError: StateFlow<String?> = _fatalError

    init {
        GlobalScope.launch {
            // First get APK path
            val apkRemotePath = adbRepo.getApkPath(androidDevice, androidApp)
            if (apkRemotePath != null) {

                val destinationFile = kotlin.io.path.createTempFile(
                    suffix = ".apk"
                ).toFile()

                println("Path is ${destinationFile.absolutePath}")

                var isDecompileStarted = false
                adbRepo.pullFile(
                    androidDevice,
                    apkRemotePath,
                    destinationFile
                ).collect {
                    println("$it % -> ${destinationFile.absolutePath}")
                    if (it == 100 && isDecompileStarted.not()) {
                        isDecompileStarted = true

                        // Now let's decompile
                        val decompiledDir = apkToolRepo.decompile(destinationFile)
                        val report = apkAnalyzerRepo.analyze(decompiledDir, librariesRepo.getCachedLibraries()!!)
                    }
                }
            } else {
                _fatalError.value = R.string.app_detail_error_apk_remote_path
            }
        }
    }
}