package com.theapache64.stackzy.test

import com.theapache64.stackzy.di.module.ApkToolModule
import com.theapache64.stackzy.di.module.NetworkModule
import com.theapache64.stackzy.di.module.PreferenceModule
import it.cosenonjaviste.daggermock.DaggerMockRule

class MyDaggerMockRule : DaggerMockRule<TestComponent>(
    TestComponent::class.java,
    NetworkModule(),
    ApkToolModule(),
    PreferenceModule(),
) {
    init {
        customizeBuilder<DaggerTestComponent.Builder> {
            it
        }
    }
}