package com.theapache64.stackzy.ui.feature.selectdevice

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.ui.navigation.Component
import javax.inject.Inject

class SelectDeviceScreenComponent(
    appComponent: AppComponent,
    private val componentContext: ComponentContext,
    private val onDeviceSelected: (AndroidDevice) -> Unit,
    private val onBackClicked: () -> Unit
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var selectDeviceViewModel: SelectDeviceViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    override fun render() {
        SelectDeviceScreen(
            selectDeviceViewModel,
            onBackClicked = onBackClicked,
            onDeviceSelected = {
                onDeviceSelected(it)
            }
        )
    }

}