package com.theapache64.stackzy

import com.theapache64.cyclone.core.Application
import com.theapache64.stackzy.ui.feature.MainActivity
import com.toxicbakery.logging.Arbor
import com.toxicbakery.logging.Seedling

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Arbor.sow(Seedling())

        val splashIntent = MainActivity.getStartIntent()
        startActivity(splashIntent)
    }
}

/**
 * The magic beings here
 */
fun main() {
    App().onCreate()
}