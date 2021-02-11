package com.theapache64.stackzy.ui.feature.device

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.theapache64.stackzy.ui.navigation.Component
import javax.inject.Inject

class DeviceScreenComponent(
    private val componentContext: ComponentContext
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var deviceViewModel: DeviceViewModel

    init {
        DaggerDeviceComponent.create().inject(this)
    }

    @Composable
    override fun render() {
        DeviceScreen(deviceViewModel)
    }

}