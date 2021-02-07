package com.theapache64.stackzy.ui.feature.splash

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.theapache64.stackzy.ui.navigation.Component

class SplashScreenComponent(
    private val componentContext: ComponentContext
) : Component, ComponentContext by componentContext {

    @Composable
    override fun render() {
        SplashScreen()
    }
}