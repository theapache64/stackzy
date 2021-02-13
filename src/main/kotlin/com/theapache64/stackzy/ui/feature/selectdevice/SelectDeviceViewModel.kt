package com.theapache64.stackzy.ui.feature.selectdevice

import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.data.repo.AdbRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectDeviceViewModel @Inject constructor(
    private val adbRepo: AdbRepo
) {

    private val _connectedDevices = MutableStateFlow<List<AndroidDevice>?>(null)
    val connectedDevices: StateFlow<List<AndroidDevice>?> = _connectedDevices


    fun watchConnectedDevices() {
        println("Adding watcher")
        GlobalScope.launch {
            adbRepo.watchConnectedDevice()
                .catch {
                    println("Error: ${it.message}")
                }
                .collect {
                    println("Devices : ${it}")
                    _connectedDevices.value = it
                }
        }
    }


    fun removeConnectionWatcher() {
        println("Removing watcher")
        adbRepo.cancelWatchConnectedDevice()
    }

}