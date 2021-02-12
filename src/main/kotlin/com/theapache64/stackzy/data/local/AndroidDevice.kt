package com.theapache64.stackzy.data.local

import com.malinskiy.adam.request.device.Device

data class AndroidDevice(
    val name: String,
    val model: String,
    val device: Device
)