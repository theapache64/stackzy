package com.theapache64.stackzy.ui.feature.selectapp

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.ui.navigation.Component
import javax.inject.Inject

class SelectAppScreenComponent(
    val selectedDevice: AndroidDevice,
    componentContext: ComponentContext,
    onAppSelected: (AndroidApp) -> Unit
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var selectAppViewModel: SelectAppViewModel

    init {
        DaggerSelectAppComponent
            .create()
            .inject(this)
    }

    @Composable
    override fun render() {
        SelectAppScreen(
            androidDevice = selectedDevice,
            selectAppViewModel = selectAppViewModel
        )
    }
}
