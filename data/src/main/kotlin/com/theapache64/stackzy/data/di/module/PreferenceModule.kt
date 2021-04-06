package com.theapache64.stackzy.data.di.module

import dagger.Module
import dagger.Provides
import java.util.prefs.Preferences
import javax.inject.Singleton

@Module
class PreferenceModule {

    @Singleton
    @Provides
    fun providePreference(): Preferences {
        return Preferences.userRoot().node("Stackzy")
    }
}