package com.theapache64.stackzy.test

import com.theapache64.stackzy.di.module.JadxModule
import com.theapache64.stackzy.di.module.NetworkModule
import com.theapache64.stackzy.di.module.PreferenceModule
import it.cosenonjaviste.daggermock.DaggerMockRule

class MyDaggerMockRule : DaggerMockRule<TestComponent>(
    TestComponent::class.java,
    NetworkModule(),
    PreferenceModule(),
    JadxModule()
) {
    init {
        customizeBuilder<DaggerTestComponent.Builder> {
            it
        }
    }
}