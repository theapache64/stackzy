package com.theapache64.stackzy.ui.feature.devicelist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.model.AndroidDeviceWrapper
import com.theapache64.stackzy.ui.navigation.Component
import com.toxicbakery.logging.Arbor
import javax.inject.Inject

class DeviceListScreenComponent(
    appComponent: AppComponent,
    private val componentContext: ComponentContext,
    private val onDeviceSelected: (AndroidDeviceWrapper) -> Unit,
    private val onBackClicked: () -> Unit
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var deviceListViewModel: DeviceListViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    override fun render() {

        val scope = rememberCoroutineScope()
        DisposableEffect(deviceListViewModel) {
            Arbor.d("Init scope")
            deviceListViewModel.init(scope)
            deviceListViewModel.watchConnectedDevices()
            onDispose {
                Arbor.d("Dispose scope")
                deviceListViewModel.stopWatchConnectedDevices()
            }
        }

        SelectDeviceScreen(
            deviceListViewModel,
            onBackClicked = onBackClicked,
            onDeviceSelected = {
                onDeviceSelected(it)
            }
        )
    }

}