package com.theapache64.stackzy.ui.feature.selectapp

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.ui.navigation.Component
import javax.inject.Inject

class SelectAppScreenComponent(
    componentContext: ComponentContext,
    val selectedDevice: AndroidDevice,
    val onAppSelected: (AndroidApp) -> Unit,
    val onBackClicked: () -> Unit
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var selectAppViewModel: SelectAppViewModel

    init {
        DaggerSelectAppComponent
            .builder()
            .androidDevice(selectedDevice)
            .build()
            .inject(this)
    }

    @Composable
    override fun render() {
        SelectAppScreen(
            selectAppViewModel = selectAppViewModel,
            onBackClicked = onBackClicked,
            onAppSelected = onAppSelected
        )
    }
}
