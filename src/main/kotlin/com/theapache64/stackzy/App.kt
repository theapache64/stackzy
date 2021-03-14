package com.theapache64.stackzy

import com.squareup.moshi.Moshi
import com.theapache64.cyclone.core.Application
import com.theapache64.stackzy.data.local.AppArgs
import com.theapache64.stackzy.data.local.AppArgsJsonAdapter
import com.theapache64.stackzy.ui.feature.MainActivity
import com.toxicbakery.logging.Arbor
import com.toxicbakery.logging.Seedling


class App(
    appArgs: AppArgs
) : Application() {

    companion object {
        // GLOBAL CONFIGS
        const val CUSTOM_TOOLBAR =
            true // TODO: Should be managed according to platform. As of now, in Windows the window is not resizable.
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
fun main(args: Array<String>) {
    // Parsing application arguments
    val argsJson = args.first()
    val appArgs = AppArgsJsonAdapter(Moshi.Builder().build()).fromJson(argsJson)!!

    // Passing args
    App(appArgs).onCreate()
}