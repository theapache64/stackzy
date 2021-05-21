package com.theapache64.stackzy.data.repo

import com.theapache64.stackzy.di.JadxDirPath
import com.theapache64.stackzy.data.util.CommandExecutor
import com.theapache64.stackzy.util.OSType
import com.theapache64.stackzy.util.OsCheck
import java.io.File
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.absolutePathString
import kotlin.io.path.div

class JadxRepo @Inject constructor(
    @JadxDirPath val jadxDirPath: Path
) {
    fun open(
        apkFile: File
    ) {
        val jadxPath = jadxDirPath / "bin" / "jadx-gui"
        val jadX = if (OsCheck.operatingSystemType == OSType.Windows) {
            "${jadxPath.absolutePathString()}.bat" // execute bat
        } else {
            "sh '${jadxPath.absolutePathString()}'" // execute shell script
        }
        val command = "$jadX '${apkFile.absolutePath}'"
        CommandExecutor.executeCommand(
            command,
            isLivePrint = false,
            isSkipException = true
        )
    }
}