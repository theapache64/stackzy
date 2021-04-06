package com.theapache64.stackzy.ui.feature.selectapp

import com.github.theapache64.gpa.api.Play
import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.data.repo.AdbRepo
import com.theapache64.stackzy.data.repo.PlayStoreRepo
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.theapache64.stackzy.model.AndroidAppWrapper
import com.theapache64.stackzy.model.AndroidDeviceWrapper
import com.theapache64.stackzy.util.ApkSource
import kotlinx.coroutines.CoroutineScope
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

    companion object {
        const val TAB_NO_TAB = -1
        private const val TAB_THIRD_PARTY_APPS_ID = 0
        private const val TAB_SYSTEM_APPS_ID = 1

        val tabsMap = mapOf(
            TAB_THIRD_PARTY_APPS_ID to "3rd Party Apps",
            TAB_SYSTEM_APPS_ID to "System Apps"
        )

        private const val MSG_LOADING_TRENDING_APPS = "Loading trending apps..."
    }

    private lateinit var viewModelScope: CoroutineScope
    private var searchJob: Job? = null

    /**
     * APK source can be either from an AndroidDevice or from a google Account
     */
    private lateinit var apkSource: ApkSource<AndroidDeviceWrapper, Account>
    private var selectedDevice: AndroidDevice? = null

    /**
     * To store all apps instaled in the device (used for search filtering)
     */
    private var fullApps: List<AndroidApp>? = null
    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword: StateFlow<String> = _searchKeyword

    private val _selectedTabIndex = MutableStateFlow(TAB_NO_TAB)
    val selectedTabIndex: StateFlow<Int> = _selectedTabIndex

    /**
     * Filtered apps
     */
    private val _apps = MutableStateFlow<Resource<List<AndroidAppWrapper>>?>(null)
    val apps: StateFlow<Resource<List<AndroidAppWrapper>>?> = _apps

    fun init(
        scope: CoroutineScope,
        apkSource: ApkSource<AndroidDeviceWrapper, Account>
    ) {
        this.viewModelScope = scope
        this.apkSource = apkSource
    }

    fun loadApps() {

        // Updating state
        _apps.value = Resource.Loading(MSG_LOADING_TRENDING_APPS)

        when (apkSource) {
            is ApkSource.Adb -> {
                // ### ADB ###
                this.selectedDevice = (apkSource as ApkSource.Adb<AndroidDevice>).value
                viewModelScope.launch {
                    fullApps = adbRepo.getInstalledApps(selectedDevice!!.device)
                    val tab = if (selectedTabIndex.value == TAB_NO_TAB) {
                        TAB_THIRD_PARTY_APPS_ID // first time
                    } else {
                        _selectedTabIndex.value // going back from detail page
                    }
                    onTabClicked(tab)
                }
            }
            is ApkSource.PlayStore -> {

                // ### PLAY STORE ###
                /*viewModelScope.launch {
                    val api = Play.getApi((apkSource as ApkSource.PlayStore<Account>).value)
                    val apps = playStoreRepo.search(" ", api)
                    _apps.value = Resource.Success(null, apps)
                }*/
                onSearchKeywordChanged(_searchKeyword.value)
            }
        }
    }

    fun onSearchKeywordChanged(newKeyword: String) {
        _searchKeyword.value = newKeyword.trim().replace("\n", "")

        when (apkSource) {
            is ApkSource.Adb -> {
                // ### ADB ###

                // Filtering apps
                val filteredApps = fullApps
                    ?.filter {
                        // search with keyword
                        it.appPackage.name.toLowerCase().contains(newKeyword, ignoreCase = true)
                    }
                    ?.filter {
                        // filter for active tab
                        it.isSystemApp == (selectedTabIndex.value == TAB_SYSTEM_APPS_ID)
                    }
                    ?: listOf()

                _apps.value = Resource.Success(null, filteredApps.map { AndroidAppWrapper(it) })
            }
            is ApkSource.PlayStore -> {
                // Play Store
                searchJob?.cancel()
                println("initiating search")
                searchJob = viewModelScope.launch {
                    println("Waiting for delay")
                    delay(500)
                    println("Delay done.. let's search ${searchKeyword.value}")
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
                        MSG_LOADING_TRENDING_APPS
                    } else {
                        "Searching for '$keyword'"
                    }

                    _apps.value = Resource.Loading(loadingMsg)

                    val apps = playStoreRepo.search(keyword, api)
                    _apps.value = Resource.Success(null, apps.map { AndroidAppWrapper(it) })
                }
            }
        }
    }

    fun onOpenMarketClicked() {
        when (apkSource) {
            is ApkSource.Adb -> {
                viewModelScope.launch {
                    val androidDevice = (apkSource as ApkSource.Adb<AndroidDevice>).value
                    adbRepo.launchMarket(androidDevice, searchKeyword.value)
                }
            }
            is ApkSource.PlayStore -> TODO()
        }

    }

    /**
     * Invoked when any of the tabs clicked
     */
    fun onTabClicked(tabIndex: Int) {
        _selectedTabIndex.value = tabIndex

        // utilising existing filter logic : since we've already filter logic inside search,
        // passing empty string would filter the tab accordingly and passing search keyword would
        // filter the apps accordingly.
        onSearchKeywordChanged(searchKeyword.value)
    }
}
