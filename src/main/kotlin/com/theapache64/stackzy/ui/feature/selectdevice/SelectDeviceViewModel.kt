package com.theapache64.stackzy.ui.feature.selectdevice

import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.data.repo.AdbRepo
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class SelectDeviceViewModel @Inject constructor(
    private val adbRepo: AdbRepo
) {

    val connectedDevices = adbRepo.watchConnectedDevice()
        .catch {
            println("Error: ${it.message}")
        }


    fun onDeviceSelected(selectedDevice: AndroidDevice) {

    }

}