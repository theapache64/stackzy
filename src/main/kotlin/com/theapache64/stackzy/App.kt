package com.theapache64.stackzy

import com.bugsnag.Bugsnag
import com.theapache64.cyclone.core.Application
import com.theapache64.stackzy.data.local.AppArgs
import com.theapache64.stackzy.ui.feature.MainActivity
import com.toxicbakery.logging.Arbor
import com.toxicbakery.logging.Seedling


val bugsnag = Bugsnag("c98f629ae7f3f708bc9ce9bf5d6b8342")

class App(
    appArgs: AppArgs
) : Application() {

    companion object {
        // GLOBAL CONFIGS
        lateinit var appArgs: AppArgs
    }

    init {
        App.appArgs = appArgs
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
    // Parsing application arguments
    val appArgs = AppArgs(
        appName = "Stackzy",
        version = "v1.2.7", // TODO: Change below date also
        versionCode = 20250119
    )

    // Passing args
    App(appArgs).onCreate()
}
