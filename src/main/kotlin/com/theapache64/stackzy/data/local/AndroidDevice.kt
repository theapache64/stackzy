package com.theapache64.stackzy.data.local

import com.malinskiy.adam.request.device.Device
import com.theapache64.stackzy.ui.common.Selectable

data class AndroidDevice(
    val name: String,
    val model: String,
    val device: Device
) : Selectable {
    override fun getTitle(): String {
        return model
    }

}