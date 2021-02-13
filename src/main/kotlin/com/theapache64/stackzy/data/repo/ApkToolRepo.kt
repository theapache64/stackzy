package com.theapache64.stackzy.data.repo

import com.theapache64.stackzy.di.ApkToolJarFile
import com.theapache64.stackzy.util.CommandExecutor
import java.io.File
import javax.inject.Inject
import kotlin.io.path.createTempDirectory

class ApkToolRepo @Inject constructor(
    @ApkToolJarFile
    private val apkToolJarFile: File
) {


    fun decompile(destinationFile: File): File {

        val tempDir = createTempDirectory().toFile()
        val command =
            "java -jar '${apkToolJarFile.absolutePath}' d '${destinationFile.absolutePath}' -o '${tempDir.absolutePath}' -f"
        println("Decompiling : \n$command")
        CommandExecutor.executeCommand(command)
        return tempDir
    }
}