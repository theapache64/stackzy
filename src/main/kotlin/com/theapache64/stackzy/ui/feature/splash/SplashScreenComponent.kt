package com.theapache64.stackzy.ui.feature.splash

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.theapache64.stackzy.ui.navigation.Component
import javax.inject.Inject

class SplashScreenComponent(
    private val componentContext: ComponentContext,
    private val onSyncFinished: () -> Unit
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var splashViewModel: SplashViewModel

    init {
        DaggerSplashComponent
            .create()
            .inject(this)
    }

    @Composable
    override fun render() {
        SplashScreen(
            splashViewModel = splashViewModel,
            onSyncFinished = onSyncFinished
        )
    }

}