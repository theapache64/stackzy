package com.theapache64.stackzy.ui.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.theapache64.stackzy.ui.feature.splash.SplashScreenComponent

class NavHostComponent(
    private val componentContext: ComponentContext
) : Component, ComponentContext by componentContext {

    private sealed class Config : Parcelable {
        object Splash : Config()
    }

    private val router = router<Config, Component>(
        initialConfiguration = Config.Splash,
        componentFactory = ::createScreenComponent
    )

    private fun createScreenComponent(config: Config, componentContext: ComponentContext): Component {
        return when (config) {
            Config.Splash -> SplashScreenComponent(
                componentContext
            )
        }
    }

    @Composable
    override fun render() {
        Children(routerState = router.state) { child, _ ->
            child.render()
        }
    }
}