package com.theapache64.stackzy.ui.feature.selectapp

import com.github.theapache64.gpa.api.Play
import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.data.repo.AdbRepo
import com.theapache64.stackzy.data.repo.PlayStoreRepo
import com.theapache64.stackzy.util.ApkSource
import com.theapache64.stackzy.util.calladapter.flow.Resource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectAppViewModel @Inject constructor(
    private val adbRepo: AdbRepo,
    private val playStoreRepo: PlayStoreRepo
) {

    private var searchJob: Job? = null

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
    private val _apps = MutableStateFlow<Resource<List<AndroidApp>>?>(null)
    val apps: StateFlow<Resource<List<AndroidApp>>?> = _apps

    fun init(apkSource: ApkSource<AndroidDevice, Account>) {
        this.apkSource = apkSource

        // Updating state
        _apps.value = Resource.Loading("Loading trending apps...")

        when (apkSource) {
            is ApkSource.Adb -> {
                // ### ADB ###
                this.selectedDevice = apkSource.value
                GlobalScope.launch {
                    fullApps = adbRepo.getInstalledApps(selectedDevice!!.device).also {
                        _apps.value = Resource.Success(null, it)
                    }
                }
            }
            is ApkSource.PlayStore -> {

                // ### PLAY STORE ###
                GlobalScope.launch {
                    val api = Play.getApi(apkSource.value)
                    val apps = playStoreRepo.search(" ", api)
                    _apps.value = Resource.Success(null, apps)
                }
            }
        }
    }

    fun onSearchKeywordChanged(newKeyword: String) {
        _searchKeyword.value = newKeyword.trim().replace("\n", "")

        when (apkSource) {
            is ApkSource.Adb -> {
                // ### ADB ###

                // Filtering apps
                val filteredApps = fullApps?.filter {
                    it.appPackage.name.toLowerCase().contains(newKeyword, ignoreCase = true)
                } ?: listOf()

                _apps.value = Resource.Success(null, filteredApps)
            }
            is ApkSource.PlayStore -> {
                // Play Store
                searchJob?.cancel()
                searchJob = GlobalScope.launch {
                    delay(500)
                    val account = (apkSource as ApkSource.PlayStore<Account>).value
                    val api = Play.getApi(account)
                    val keyword = searchKeyword.value.let {
                        if (it.isBlank()) {
                            " "
                        } else {
                            it
                        }
                    }
                    val loadingMsg = if (keyword.isBlank()) {
                        "Loading trending apps"
                    } else {
                        "Searching for '$keyword'"
                    }

                    _apps.value = Resource.Loading(loadingMsg)

                    val apps = playStoreRepo.search(keyword, api)
                    _apps.value = Resource.Success(null, apps)
                }
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
