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
    val selectedDevice: AndroidDevice,
    val adbRepo: AdbRepo
) {
    private val _apps = MutableStateFlow(listOf<AndroidApp>())
    val apps: StateFlow<List<AndroidApp>> = _apps

    init {
        GlobalScope.launch {

        }
    }
}