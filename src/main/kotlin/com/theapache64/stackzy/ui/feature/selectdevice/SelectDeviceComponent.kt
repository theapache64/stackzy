package com.theapache64.stackzy.ui.feature.selectdevice

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component()
interface SelectDeviceComponent {
    fun inject(selectDeviceScreenComponent: SelectDeviceScreenComponent)
}