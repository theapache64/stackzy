package com.theapache64.stackzy.di.module

import com.theapache64.stackzy.di.AdbFile
import dagger.Module
import dagger.Provides
import java.io.File
import java.io.IOException

@Module
class AdbModule {

    companion object {
        private val adbFile = File("adb")
        private const val ADB_RES_NAME = "adb"
    }

    @Provides
    @AdbFile
    fun provideAdb(): File {
        if (adbFile.exists().not()) {
            val adbResStream = this::class.java.classLoader.getResourceAsStream(ADB_RES_NAME)
            if (adbResStream != null) {
                adbFile.writeBytes(adbResStream.readAllBytes())
                adbFile.setExecutable(true)
            } else {
                throw IOException("Failed to parse adb from resources")
            }
        }

        return adbFile
    }


}