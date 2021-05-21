package com.theapache64.stackzy.ui.feature.selectdevice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.model.AndroidDeviceWrapper
import com.theapache64.stackzy.ui.navigation.Component
import com.toxicbakery.logging.Arbor
import javax.inject.Inject

class SelectDeviceScreenComponent(
    appComponent: AppComponent,
    private val componentContext: ComponentContext,
    private val onDeviceSelected: (AndroidDeviceWrapper) -> Unit,
    private val onBackClicked: () -> Unit
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var selectDeviceViewModel: SelectDeviceViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    override fun render() {

        val scope = rememberCoroutineScope()
        DisposableEffect(selectDeviceViewModel) {
            Arbor.d("Init scope")
            selectDeviceViewModel.init(scope)
            selectDeviceViewModel.watchConnectedDevices()
            onDispose {
                Arbor.d("Dispose scope")
                selectDeviceViewModel.stopWatchConnectedDevices()
            }
        }

        SelectDeviceScreen(
            selectDeviceViewModel,
            onBackClicked = onBackClicked,
            onDeviceSelected = {
                onDeviceSelected(it)
            }
        )
    }

}