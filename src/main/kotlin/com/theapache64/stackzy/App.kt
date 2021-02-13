package com.theapache64.stackzy

import com.theapache64.cyclone.core.Application
import com.theapache64.stackzy.ui.feature.MainActivity
import com.theapache64.stackzy.util.CommandExecutor
import com.toxicbakery.logging.Arbor
import com.toxicbakery.logging.Seedling
import java.io.File

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Arbor.sow(Seedling())

        val splashIntent = MainActivity.getStartIntent()
        startActivity(splashIntent)
    }
}

fun main() {
    App().onCreate()
    val apkTool = App::class.java.classLoader.getResourceAsStream("apktool_2.5.0.jar")
    val targetFile = File("my_apk_tool.jar")
    targetFile.writeBytes(apkTool!!.readAllBytes())
    println(targetFile.exists())
    println(targetFile.absolutePath)
    println(CommandExecutor.executeCommand("java -jar ${targetFile.absolutePath}", false, true))
}