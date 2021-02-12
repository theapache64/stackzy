package com.theapache64.stackzy.ui.feature.selectapp

import com.theapache64.stackzy.data.local.AndroidDevice
import dagger.BindsInstance
import dagger.Component

@Component
interface SelectAppComponent {
    fun inject(selectAppScreenComponent: SelectAppScreenComponent)

    @Component.Builder
    interface Builder {
        fun build(): SelectAppComponent

        @BindsInstance
        fun androidDevice(androidDevice: AndroidDevice) : Builder
    }
}