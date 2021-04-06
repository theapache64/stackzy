package com.theapache64.stackzy.data.di.module

import com.theapache64.stackzy.data.di.ApkToolJarFile
import dagger.Module
import dagger.Provides
import java.io.File
import java.io.IOException

@Module
class ApkToolModule {

    companion object {
        private val apkToolJar = File("apk-tool.jar")
        private const val APKTOOL_JAR_NAME = "apktool_2.5.0.jar"
    }

    @Provides
    @ApkToolJarFile
    fun provideApkToolJarFile(): File {
        if (apkToolJar.exists().not()) {
            val apkToolStream = this::class.java.classLoader.getResourceAsStream(APKTOOL_JAR_NAME)
            if (apkToolStream != null) {
                apkToolJar.writeBytes(apkToolStream.readAllBytes())
            } else {
                throw IOException("Failed to parse apk-tool from resources")
            }
        }

        return apkToolJar
    }
}