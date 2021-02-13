package com.theapache64.stackzy.ui.feature.selectapp

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.ui.navigation.Component
import javax.inject.Inject

class SelectAppScreenComponent(
    componentContext: ComponentContext,
    appComponent: AppComponent,
    val selectedDevice: AndroidDevice,
    val onAppSelected: (AndroidDevice, AndroidApp) -> Unit,
    val onBackClicked: () -> Unit
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var selectAppViewModel: SelectAppViewModel

    init {
        appComponent.inject(this)
        selectAppViewModel.init(selectedDevice)
    }

    @Composable
    override fun render() {
        SelectAppScreen(
            selectAppViewModel = selectAppViewModel,
            onBackClicked = onBackClicked,
            onAppSelected = {
                onAppSelected(selectedDevice, it)
            }
        )
    }
}
