package com.theapache64.stackzy.test

import com.theapache64.stackzy.data.di.module.ApkToolModule
import com.theapache64.stackzy.data.di.module.JadxModule
import com.theapache64.stackzy.data.di.module.NetworkModule
import com.theapache64.stackzy.data.di.module.PreferenceModule
import it.cosenonjaviste.daggermock.DaggerMockRule

class MyDaggerMockRule : DaggerMockRule<TestComponent>(
    TestComponent::class.java,
    NetworkModule(),
    ApkToolModule(),
    PreferenceModule(),
    JadxModule()
) {
    init {
        customizeBuilder<DaggerTestComponent.Builder> {
            it
        }
    }
}