package com.theapache64.stackzy.ui.feature.selectapp

import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.data.repo.AdbRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectAppViewModel @Inject constructor(
    val adbRepo: AdbRepo
) {

    private var fullApps: List<AndroidApp>? = null
    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword: StateFlow<String> = _searchKeyword

    private val _apps = MutableStateFlow(listOf<AndroidApp>())
    val apps: StateFlow<List<AndroidApp>> = _apps

    fun init(selectedDevice: AndroidDevice) {
        GlobalScope.launch {
            fullApps = adbRepo.getInstalledApps(selectedDevice.device)
            _apps.value = fullApps!!
        }
    }

    fun onSearchKeywordChanged(newKeyword: String) {
        _searchKeyword.value = newKeyword
        _apps.value = fullApps!!.filter { it.appPackage.name.toLowerCase().contains(newKeyword, ignoreCase = true) }
    }
}
