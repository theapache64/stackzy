package com.theapache64.stackzy.di.component

import com.theapache64.stackzy.ui.feature.splash.SplashScreenComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface SplashComponent {
    fun inject(splashScreenComponent: SplashScreenComponent)
}