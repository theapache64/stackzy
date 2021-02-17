package com.theapache64.stackzy.di

import com.theapache64.stackzy.di.module.AdbModule
import com.theapache64.stackzy.di.module.ApkToolModule
import com.theapache64.stackzy.di.module.NetworkModule
import com.theapache64.stackzy.ui.feature.appdetail.AppDetailScreenComponent
import com.theapache64.stackzy.ui.feature.selectapp.SelectAppScreenComponent
import com.theapache64.stackzy.ui.feature.selectdevice.SelectDeviceScreenComponent
import com.theapache64.stackzy.ui.feature.splash.SplashScreenComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        ApkToolModule::class,
        AdbModule::class
    ]
)
interface AppComponent {
    fun inject(splashScreenComponent: SplashScreenComponent)
    fun inject(selectAppScreenComponent: SelectAppScreenComponent)
    fun inject(appDetailScreenComponent: AppDetailScreenComponent)
    fun inject(selectDeviceScreenComponent: SelectDeviceScreenComponent)
}