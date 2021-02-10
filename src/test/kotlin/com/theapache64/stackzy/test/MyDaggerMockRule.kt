package com.theapache64.stackzy.test

import com.theapache64.stackzy.di.module.NetworkModule
import it.cosenonjaviste.daggermock.DaggerMockRule

class MyDaggerMockRule : DaggerMockRule<TestComponent>(
    TestComponent::class.java,
    NetworkModule()
) {
    init {
        customizeBuilder<DaggerTestComponent.Builder> {
            it
        }
    }
}