package com.theapache64.stackzy

import com.theapache64.cyclone.core.Application
import com.theapache64.stackzy.ui.feature.MainActivity
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val splashIntent = MainActivity.getStartIntent()
        startActivity(splashIntent)
    }
}

fun main() {
    App().onCreate()
}