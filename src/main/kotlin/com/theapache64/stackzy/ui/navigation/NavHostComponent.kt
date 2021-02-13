package com.theapache64.stackzy.ui.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.malinskiy.adam.request.device.Device
import com.malinskiy.adam.request.device.DeviceState
import com.malinskiy.adam.request.pkg.Package
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.ui.feature.appdetail.AppDetailScreenComponent
import com.theapache64.stackzy.ui.feature.selectapp.SelectAppScreenComponent
import com.theapache64.stackzy.ui.feature.selectdevice.SelectDeviceScreenComponent
import com.theapache64.stackzy.ui.feature.splash.SplashScreenComponent

class NavHostComponent(
    private val componentContext: ComponentContext
) : Component, ComponentContext by componentContext {

    private sealed class Config : Parcelable {
        object Splash : Config()
        object SelectDevice : Config()
        data class SelectApp(val androidDevice: AndroidDevice) : Config()
        data class AppDetail(
            val androidDevice: AndroidDevice,
            val androidApp: AndroidApp
        ) : Config()
    }

    private val router = router<Config, Component>(
        // initialConfiguration = Config.SelectDevice,
        initialConfiguration = Config.AppDetail(
            AndroidDevice(
                "Samsung",
                "someModel",
                Device(
                    "R52M604X18E",
                    DeviceState.DEVICE
                )
            ),
            AndroidApp(
                Package("com.theapache64.papercop")
            )
        ),
        componentFactory = ::createScreenComponent
    )

    private fun createScreenComponent(config: Config, componentContext: ComponentContext): Component {
        return when (config) {
            is Config.Splash -> SplashScreenComponent(
                componentContext = componentContext,
                onSyncFinished = ::onSplashSyncFinished
            )
            is Config.SelectDevice -> SelectDeviceScreenComponent(
                componentContext = componentContext,
                onDeviceSelected = ::onDeviceSelected
            )
            is Config.SelectApp -> SelectAppScreenComponent(
                componentContext = componentContext,
                selectedDevice = config.androidDevice,
                onAppSelected = ::onAppSelected,
                onBackClicked = ::onBackClicked
            )

            is Config.AppDetail -> AppDetailScreenComponent(
                componentContext = componentContext,
                selectedApp = config.androidApp,
                selectedDevice = config.androidDevice,
                onLibrarySelected = ::onLibrarySelected,
                onBackClicked = ::onBackClicked
            )
        }
    }

    @Composable
    override fun render() {
        Children(routerState = router.state) { child, _ ->
            child.render()
        }
    }

    /**
     * Invoked when splash finish data sync
     */
    private fun onSplashSyncFinished() {
        router.push(Config.SelectDevice)
    }

    /**
     * Invoked when a device selected
     */
    private fun onDeviceSelected(androidDevice: AndroidDevice) {
        router.push(Config.SelectApp(androidDevice))
    }

    /**
     * Invoked when the app got selected
     */
    private fun onAppSelected(
        androidDevice: AndroidDevice,
        androidApp: AndroidApp
    ) {
        router.push(
            Config.AppDetail(
                androidDevice = androidDevice,
                androidApp = androidApp
            )
        )
    }

    /**
     * Invoked when library selected
     */
    private fun onLibrarySelected(library: Library) {

    }

    private fun onBackClicked() {
        router.pop()
    }
}