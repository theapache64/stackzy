package com.theapache64.stackzy

import com.theapache64.cyclone.core.Application
import com.theapache64.stackzy.feature.splash.SplashActivity

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val splashIntent = SplashActivity.getStartIntent()
        startActivity(splashIntent)
    }
}

fun main() {
    App().onCreate()
}