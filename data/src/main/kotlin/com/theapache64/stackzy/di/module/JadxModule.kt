package com.theapache64.stackzy.di.module

import com.theapache64.stackzy.data.util.ResDir
import com.theapache64.stackzy.data.util.unzip
import com.theapache64.stackzy.di.JadxDirPath
import dagger.Module
import dagger.Provides
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.*

@Module
class JadxModule {

    companion object {
        private val jadxZipFile = Path(ResDir.dir) / "jadx-1.2.0.zip"
        private val jadxDirFile = Path(ResDir.dir) / "build" / jadxZipFile.nameWithoutExtension
    }

    @Provides
    @JadxDirPath
    fun provideJadXDirFile(): Path {
        if (jadxDirFile.exists().not()) {
            val jadxStream = this::class.java.classLoader.getResourceAsStream(jadxZipFile.name)
            if (jadxStream != null) {
                // Copying jadx zip to local folder
                jadxZipFile.parent.toFile().let { parentDir ->
                    if (parentDir.exists().not()) {
                        parentDir.mkdirs()
                    }
                }
                jadxDirFile.parent.toFile().let { parentDir ->
                    if (parentDir.exists().not()) {
                        parentDir.mkdirs()
                    }
                }
                jadxZipFile.writeBytes(jadxStream.readAllBytes())
                jadxZipFile.unzip(jadxDirFile)
                jadxZipFile.deleteIfExists()
            } else {
                throw IOException("Failed to parse apk-tool from resources")
            }
        }


        return jadxDirFile
    }
}