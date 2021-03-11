package com.theapache64.stackzy.ui.feature.selectapp

import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.data.repo.AdbRepo
import com.theapache64.stackzy.util.Either
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectAppViewModel @Inject constructor(
    val adbRepo: AdbRepo
) {

    private lateinit var source: Either<AndroidDevice, Account>
    private lateinit var selectedDevice: AndroidDevice

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

    fun init(source: Either<AndroidDevice, Account>) {
        this.source = source
        when (source) {
            is Either.Left -> {
                // ### ADB ###

                this.selectedDevice = source.value
                GlobalScope.launch {
                    fullApps = adbRepo.getInstalledApps(selectedDevice.device).also {
                        _apps.value = it
                    }
                }
            }
            is Either.Right -> {
                // ### PLAY STORE ###

            }
        }
    }

    fun onSearchKeywordChanged(newKeyword: String) {
        _searchKeyword.value = newKeyword.trim().replace("\n", "")

        when (source) {
            is Either.Left -> {
                // ### ADB ###

                // Filtering apps
                _apps.value =
                    fullApps?.filter {
                        it.appPackage.name.toLowerCase().contains(newKeyword, ignoreCase = true)
                    }
            }
            is Either.Right -> {
                // Play Store
            }
        }
    }

    fun onOpenMarketClicked() {
        GlobalScope.launch {
            adbRepo.launchMarket(selectedDevice, searchKeyword.value)
        }
    }
}
