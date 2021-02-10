package com.theapache64.stackzy.ui.feature.device

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component()
interface DeviceComponent {
    fun inject(deviceScreenComponent: DeviceScreenComponent)
}