package com.theapache64.stackzy.di

import com.theapache64.stackzy.data.repo.AdbRepo
import com.theapache64.stackzy.di.module.ApkToolModule
import com.theapache64.stackzy.di.module.NetworkModule
import com.theapache64.stackzy.ui.feature.appdetail.AppDetailScreenComponent
import com.theapache64.stackzy.ui.feature.login.LogInScreenComponent
import com.theapache64.stackzy.ui.feature.pathway.PathwayScreenComponent
import com.theapache64.stackzy.ui.feature.selectapp.SelectAppScreenComponent
import com.theapache64.stackzy.ui.feature.selectdevice.SelectDeviceScreenComponent
import com.theapache64.stackzy.ui.feature.splash.SplashScreenComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        ApkToolModule::class
    ]
)
interface AppComponent {
    fun inject(splashScreenComponent: SplashScreenComponent)
    fun inject(selectPathwayScreenComponent: PathwayScreenComponent)
    fun inject(logInScreenComponent: LogInScreenComponent)
    fun inject(selectAppScreenComponent: SelectAppScreenComponent)
    fun inject(appDetailScreenComponent: AppDetailScreenComponent)
    fun inject(selectDeviceScreenComponent: SelectDeviceScreenComponent)

    // bind repo to this component
    fun bind(): AdbRepo
}