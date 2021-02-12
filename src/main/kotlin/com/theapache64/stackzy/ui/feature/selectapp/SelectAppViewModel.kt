package com.theapache64.stackzy.ui.feature.selectapp

import com.theapache64.stackzy.data.local.AndroidDevice
import javax.inject.Inject

class SelectAppViewModel @Inject constructor(
    val selectedDevice: AndroidDevice
) {
}