package com.theapache64.stackzy.data.repo

import com.theapache64.stackzy.data.util.CommandExecutor
import com.theapache64.stackzy.di.JadxDirPath
import com.theapache64.stackzy.util.OSType
import com.theapache64.stackzy.util.OsCheck
import com.toxicbakery.logging.Arbor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempDirectory
import kotlin.io.path.div

class JadxRepo @Inject constructor(
    @JadxDirPath val jadxDirPath: Path
) {
    fun open(
        apkFile: File
    ) {
        val command = "${getJadX(isGui = true)} '${apkFile.absolutePath}'"
        CommandExecutor.executeCommand(
            command,
            isLivePrint = false,
            isSkipException = true
        )
    }

    private fun getJadX(
        isGui: Boolean = false
    ): String {
        val jadxPath = jadxDirPath / "bin" / if (isGui) {
            "jadx-gui"
        } else {
            "jadx"
        }
        return if (OsCheck.operatingSystemType == OSType.Windows) {
            "${jadxPath.absolutePathString()}.bat" // execute bat
        } else {
            "sh '${jadxPath.absolutePathString()}'" // execute shell script
        }
    }


    suspend fun decompile(
        destinationFile: File,
        targetDir: File = createTempDirectory().toFile(),
        onDecompileMessage: ((String) -> Unit)? = null
    ): File = withContext(Dispatchers.IO) {
        val command = """${getJadX()} "${destinationFile.absolutePath}" -d ${targetDir.absolutePath}"""
        Arbor.d("Decompiling : \n$command && code-insiders '${targetDir.absolutePath}'")
        CommandExecutor.executeCommand(
            command = command,
            isSkipException = false,
            isLivePrint = true,
            onPrintLine = onDecompileMessage
        )
        targetDir
    }


}