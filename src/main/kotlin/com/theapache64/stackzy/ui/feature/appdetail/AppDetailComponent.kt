package com.theapache64.stackzy.ui.feature.appdetail

import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.di.module.ApkToolModule
import com.theapache64.stackzy.di.module.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ApkToolModule::class])
interface AppDetailComponent {

    fun inject(appDetailScreenComponent: AppDetailScreenComponent)

    @Component.Builder
    interface Builder {
        fun build(): AppDetailComponent

        @BindsInstance
        fun androidApp(androidApp: AndroidApp): Builder

        @BindsInstance
        fun androidDevice(androidDevice: AndroidDevice): Builder
    }
}