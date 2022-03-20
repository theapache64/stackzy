package com.theapache64.stackzy.model

import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.data.local.AndroidDeviceDefinition

class AndroidDeviceWrapper(
    val androidDevice: AndroidDevice
) : AndroidDeviceDefinition by androidDevice, AlphabetCircle() {

    override fun getTitle() = model
    override fun getSubtitle() = name
    override fun imageUrl(): String? = null
    override fun isActive(): Boolean = false
}