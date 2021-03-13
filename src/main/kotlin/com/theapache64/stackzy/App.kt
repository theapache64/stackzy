package com.theapache64.stackzy

import com.theapache64.cyclone.core.Application
import com.theapache64.stackzy.ui.feature.MainActivity
import com.toxicbakery.logging.Arbor
import com.toxicbakery.logging.Seedling



class App : Application() {

    companion object {
        // GLOBAL CONFIGS
        const val CUSTOM_TOOLBAR = true // TODO: Should be managed according to platform. As of now, in windows,
        // the window is not resizable.
    }

    override fun onCreate() {
        super.onCreate()
        Arbor.sow(Seedling())

        val splashIntent = MainActivity.getStartIntent()
        startActivity(splashIntent)
    }
}

/**
 * The magic begins here
 */
fun main() {
    App().onCreate()
}