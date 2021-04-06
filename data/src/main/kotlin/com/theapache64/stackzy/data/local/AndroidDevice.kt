package com.theapache64.stackzy.data.local

import com.malinskiy.adam.request.device.Device

interface AndroidDeviceDefinition {
    val name: String
    val model: String
    val device: Device
}

class AndroidDevice(
    override val name: String,
    override val model: String,
    override val device: Device
) : AndroidDeviceDefinition