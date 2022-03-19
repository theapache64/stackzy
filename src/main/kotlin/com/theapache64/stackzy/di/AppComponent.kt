package com.theapache64.stackzy.di

import com.theapache64.stackzy.data.repo.AdbRepo
import com.theapache64.stackzy.di.module.*
import com.theapache64.stackzy.ui.feature.appdetail.AppDetailScreenComponent
import com.theapache64.stackzy.ui.feature.applist.AppListScreenComponent
import com.theapache64.stackzy.ui.feature.appmasterdetail.AppMasterDetailScreenComponent
import com.theapache64.stackzy.ui.feature.devicelist.DeviceListScreenComponent
import com.theapache64.stackzy.ui.feature.libdetail.LibraryDetailScreenComponent
import com.theapache64.stackzy.ui.feature.liblist.LibraryListScreenComponent
import com.theapache64.stackzy.ui.feature.login.LogInScreenComponent
import com.theapache64.stackzy.ui.feature.pathway.PathwayScreenComponent
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
    fun inject(appListScreenComponent: AppListScreenComponent)
    fun inject(appDetailScreenComponent: AppDetailScreenComponent)
    fun inject(deviceListScreenComponent: DeviceListScreenComponent)
    fun inject(updateScreenComponent: UpdateScreenComponent)
    fun inject(libraryListScreenComponent: LibraryListScreenComponent)
    fun inject(libraryDetailScreenComponent: LibraryDetailScreenComponent)
    fun inject(appMasterDetailScreenComponent: AppMasterDetailScreenComponent)

    // bind repo to this component
    fun bind(): AdbRepo
}