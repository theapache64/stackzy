package com.theapache64.stackzy.data.repo

import com.theapache64.stackzy.data.di.JadxDirPath
import com.theapache64.stackzy.data.util.CommandExecutor
import java.io.File
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.absolutePathString
import kotlin.io.path.div

class JadxRepo @Inject constructor(
    @JadxDirPath val jadxDirPath: Path
) {
    suspend fun open(
        apkFile: File
    ) {
        // TODO : Support Windows
        val jadxPath = jadxDirPath / "bin" / "jadx-gui"
        val command = "sh '${jadxPath.absolutePathString()}' '${apkFile.absolutePath}'"
        CommandExecutor.executeCommand(
            command,
            isLivePrint = false,
            isSkipException = true
        )
    }
}