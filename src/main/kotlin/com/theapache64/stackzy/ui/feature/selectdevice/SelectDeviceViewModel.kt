package com.theapache64.stackzy.ui.feature.selectdevice

import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.data.repo.AdbRepo
import com.toxicbakery.logging.Arbor
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

    /**
     * To start watching connected devices
     */
    fun watchConnectedDevices() {
        GlobalScope.launch {
            adbRepo.watchConnectedDevice()
                .catch {
                    Arbor.d("Error: ${it.message}")
                }
                .collect {
                    Arbor.d("Devices : $it")
                    _connectedDevices.value = it
                }
        }
    }


    /**
     * To stop watching connected devices
     */
    fun removeConnectionWatcher() {
        println("Removing watcher")
        adbRepo.cancelWatchConnectedDevice()
    }

}