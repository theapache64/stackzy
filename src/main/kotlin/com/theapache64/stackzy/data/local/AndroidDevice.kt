package com.theapache64.stackzy.data.local

import com.malinskiy.adam.request.device.Device
import com.theapache64.stackzy.ui.common.AlphabetCircle

data class AndroidDevice(
    val name: String,
    val model: String,
    val device: Device
) : AlphabetCircle() {

    override fun getTitle(): String {
        return model
    }

    override fun getSubtitle(): String {
        return name
    }

    override fun imageUrl(): String? = null
}