package com.theapache64.stackzy.data.repo

import com.theapache64.stackzy.data.di.ApkToolJarFile
import com.theapache64.stackzy.data.util.CommandExecutor
import com.toxicbakery.logging.Arbor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.io.path.createTempDirectory

class ApkToolRepo @Inject constructor(
    @ApkToolJarFile
    private val apkToolJarFile: File
) {

    suspend fun decompile(
        destinationFile: File,
        targetDir: File = createTempDirectory().toFile()
    ): File = withContext(Dispatchers.IO) {

        val command =
            "java -jar \"${apkToolJarFile.absolutePath}\" d \"${destinationFile.absolutePath}\" -o \"${targetDir.absolutePath}\" -f"
        Arbor.d("Decompiling : \n$command && code-insiders '${targetDir.absolutePath}'")
        CommandExecutor.executeCommand(command = command, isSkipException = false, isLivePrint = true)
        targetDir
    }
}