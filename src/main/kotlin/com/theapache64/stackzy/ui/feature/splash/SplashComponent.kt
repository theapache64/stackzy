package com.theapache64.stackzy.ui.feature.splash

import com.theapache64.stackzy.di.module.NetworkModule
import com.theapache64.stackzy.ui.feature.splash.SplashScreenComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface SplashComponent {
    fun inject(splashScreenComponent: SplashScreenComponent)
}