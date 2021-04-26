package com.theapache64.stackzy.data.di

import com.theapache64.stackzy.data.di.module.*
import com.theapache64.stackzy.data.repo.AdbRepo
import com.theapache64.stackzy.ui.feature.appdetail.AppDetailScreenComponent
import com.theapache64.stackzy.ui.feature.login.LogInScreenComponent
import com.theapache64.stackzy.ui.feature.pathway.PathwayScreenComponent
import com.theapache64.stackzy.ui.feature.selectapp.SelectAppScreenComponent
import com.theapache64.stackzy.ui.feature.selectdevice.SelectDeviceScreenComponent
import com.theapache64.stackzy.ui.feature.splash.SplashScreenComponent
import com.theapache64.stackzy.ui.feature.update.UpdateScreenComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        ApkToolModule::class,
        PreferenceModule::class,
        CryptoModule::class,
        JadxModule::class,
    ]
)
interface AppComponent {
    fun inject(splashScreenComponent: SplashScreenComponent)
    fun inject(selectPathwayScreenComponent: PathwayScreenComponent)
    fun inject(logInScreenComponent: LogInScreenComponent)
    fun inject(selectAppScreenComponent: SelectAppScreenComponent)
    fun inject(appDetailScreenComponent: AppDetailScreenComponent)
    fun inject(selectDeviceScreenComponent: SelectDeviceScreenComponent)
    fun inject(updateScreenComponent: UpdateScreenComponent)

    // bind repo to this component
    fun bind(): AdbRepo
}