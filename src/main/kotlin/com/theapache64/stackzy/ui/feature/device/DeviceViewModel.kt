package com.theapache64.stackzy.ui.feature.device

import com.malinskiy.adam.request.device.Device
import com.theapache64.stackzy.data.repo.AdbRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class DeviceViewModel @Inject constructor(
    private val adbRepo: AdbRepo
) {
    private val _connectedDevices = MutableStateFlow(listOf<Device>())
    val connectedDevices: StateFlow<List<Device>> = _connectedDevices

    init {

    }
}