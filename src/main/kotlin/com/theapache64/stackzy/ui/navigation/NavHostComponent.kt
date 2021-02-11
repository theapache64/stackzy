package com.theapache64.stackzy.ui.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.malinskiy.adam.request.device.Device
import com.theapache64.stackzy.ui.feature.device.DeviceScreenComponent
import com.theapache64.stackzy.ui.feature.splash.SplashScreenComponent

class NavHostComponent(
    private val componentContext: ComponentContext
) : Component, ComponentContext by componentContext {

    private sealed class Config : Parcelable {
        object Splash : Config()
        object Device : Config()
    }

    private val router = router<Config, Component>(
        initialConfiguration = Config.Splash,
        componentFactory = ::createScreenComponent
    )

    private fun createScreenComponent(config: Config, componentContext: ComponentContext): Component {
        return when (config) {
            Config.Splash -> SplashScreenComponent(
                componentContext = componentContext,
                onSyncFinished = ::onSplashSyncFinished
            )
            Config.Device -> DeviceScreenComponent(
                componentContext = componentContext
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
        router.push(Config.Device)
    }

    /**
     * Invoked when a device selected
     */
    private fun onDeviceSelected(device: Device) {

    }
}