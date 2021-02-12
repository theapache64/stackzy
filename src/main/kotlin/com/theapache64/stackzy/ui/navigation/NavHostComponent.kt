package com.theapache64.stackzy.ui.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
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
    }

    private val router = router<Config, Component>(
        initialConfiguration = Config.SelectDevice,
        componentFactory = ::createScreenComponent
    )

    private fun createScreenComponent(config: Config, componentContext: ComponentContext): Component {
        return when (config) {
            Config.Splash -> SplashScreenComponent(
                componentContext = componentContext,
                onSyncFinished = ::onSplashSyncFinished
            )
            Config.SelectDevice -> SelectDeviceScreenComponent(
                componentContext = componentContext,
                onDeviceSelected = ::onDeviceSelected
            )
            is Config.SelectApp -> SelectAppScreenComponent(
                selectedDevice = config.androidDevice,
                componentContext = componentContext,
                onAppSelected = ::onAppSelected
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
    private fun onAppSelected(androidApp: AndroidApp) {

    }
}