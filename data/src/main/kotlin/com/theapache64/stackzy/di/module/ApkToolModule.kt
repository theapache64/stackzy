package com.theapache64.stackzy.di.module

import com.theapache64.stackzy.data.util.ResDir
import com.theapache64.stackzy.di.ApkToolJarFile
import dagger.Module
import dagger.Provides
import java.io.File
import java.io.IOException

@Module
class ApkToolModule {

    companion object {
        private val apkToolJar = File("${ResDir.dir}${File.separator}apk-tool.jar")
        private const val APKTOOL_JAR_NAME = "apktool_2.9.3.jar"
    }

    @Provides
    @ApkToolJarFile
    fun provideApkToolJarFile(): File {
        if (apkToolJar.exists().not()) {
            val apkToolStream = this::class.java.classLoader.getResourceAsStream(APKTOOL_JAR_NAME)
            if (apkToolStream != null) {
                apkToolJar.parentFile.let { parentDir ->
                    if (parentDir.exists().not()) {
                        parentDir.mkdirs()
                    }
                }
                if (apkToolJar.createNewFile()) {
                    apkToolJar.writeBytes(apkToolStream.readAllBytes())
                } else {
                    throw IOException("Failed to create ${apkToolJar.absolutePath}")
                }
            } else {
                throw IOException("Failed to parse apk-tool from resources")
            }
        }

        return apkToolJar
    }
}