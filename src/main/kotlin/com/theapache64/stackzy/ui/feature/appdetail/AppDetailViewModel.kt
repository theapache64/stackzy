package com.theapache64.stackzy.ui.feature.appdetail

import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.data.repo.AdbRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppDetailViewModel @Inject constructor(
    private val androidDevice: AndroidDevice,
    private val androidApp: AndroidApp,
    private val adbRepo: AdbRepo
) {
    init {
        GlobalScope.launch {
            // First get APK path
            val apkPath = adbRepo.getApkPath(androidDevice, androidApp)
        }
    }
}