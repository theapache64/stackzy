package com.theapache64.stackzy

import com.theapache64.cyclone.core.Application
import com.theapache64.stackzy.data.local.AppArgs
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
fun main(_args: Array<String>) {
    // Parsing application arguments
    /*var args = _args.toList()
    if(OsCheck.operatingSystemType== OSType.Windows){
        args = args[0].split(" ")
    }*/
    val appArgs = AppArgs(
        appName = "Stackzy",
        version = "v1.0.0-beta04"
    )

    // Passing args
    App(appArgs).onCreate()
}