package com.theapache64.stackzy.ui.feature.selectapp

import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.data.repo.AdbRepo
import com.theapache64.stackzy.util.ApkSource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectAppViewModel @Inject constructor(
    val adbRepo: AdbRepo
) {

    /**
     * APK source can be either from an AndroidDevice or from a google Account
     */
    private lateinit var apkSource: ApkSource<AndroidDevice, Account>
    private var selectedDevice: AndroidDevice? = null

    /**
     * To store all apps instaled in the device (used for search filtering)
     */
    private var fullApps: List<AndroidApp>? = null
    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword: StateFlow<String> = _searchKeyword

    /**
     * Filtered apps
     */
    private val _apps = MutableStateFlow<List<AndroidApp>?>(null)
    val apps: StateFlow<List<AndroidApp>?> = _apps

    fun init(apkSource: ApkSource<AndroidDevice, Account>) {
        this.apkSource = apkSource
        when (apkSource) {
            is ApkSource.Adb -> {
                // ### ADB ###

                this.selectedDevice = apkSource.value
                GlobalScope.launch {
                    fullApps = adbRepo.getInstalledApps(selectedDevice!!.device).also {
                        _apps.value = it
                    }
                }
            }
            is ApkSource.PlayStore -> {
                // ### PLAY STORE ###

            }
        }
    }

    fun onSearchKeywordChanged(newKeyword: String) {
        _searchKeyword.value = newKeyword.trim().replace("\n", "")

        when (apkSource) {
            is ApkSource.Adb -> {
                // ### ADB ###

                // Filtering apps
                _apps.value =
                    fullApps?.filter {
                        it.appPackage.name.toLowerCase().contains(newKeyword, ignoreCase = true)
                    }
            }
            is ApkSource.PlayStore -> {
                // Play Store
            }
        }
    }

    fun onOpenMarketClicked() {
        when (apkSource) {
            is ApkSource.Adb -> {
                GlobalScope.launch {
                    val androidDevice = (apkSource as ApkSource.Adb<AndroidDevice>).value
                    adbRepo.launchMarket(androidDevice, searchKeyword.value)
                }
            }
            is ApkSource.PlayStore -> TODO()
        }

    }
}
