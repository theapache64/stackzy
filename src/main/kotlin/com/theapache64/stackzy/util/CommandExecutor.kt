package com.theapache64.stackzy.util

import com.toxicbakery.logging.Arbor
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * To execute commands programmatically
 */
object CommandExecutor {

    @Throws
    fun executeCommand(command: String): String = executeCommand(command, isLivePrint = false, isSkipException = false)

    @Throws
    fun executeCommand(command: String, isLivePrint: Boolean, isSkipException: Boolean): String =
        executeCommands(arrayOf(command), isLivePrint, isSkipException).joinToString(separator = "\n")


    @Throws(IOException::class)
    fun executeCommands(commands: Array<String>, isLivePrint: Boolean, isSkipException: Boolean): List<String> {

        val rt = Runtime.getRuntime()

        val proc = if (OsCheck.operatingSystemType == OSType.Windows) {
            // direct execution
            rt.exec(commands.joinToString(separator = " "))
        } else {
            // execute via shell
            rt.exec(
                arrayOf(
                    "/bin/sh", "-c", *commands
                )
            )
        }

        val stdInput = BufferedReader(InputStreamReader(proc.inputStream))
        val stdError = BufferedReader(InputStreamReader(proc.errorStream))

        // Read the output from the command
        var lastLine: String?
        val result = mutableListOf<String>()
        stdInput.forEachLine { line ->
            if (isLivePrint) {
                Arbor.d(line)
            }
            result.add(line)
        }

        // Read any errors from the attempted command
        val error = StringBuilder()
        stdError.forEachLine { line ->
            if (isLivePrint) {
                Arbor.d(line)
            }
            error.append(line).append("\n")
        }

        if (!isSkipException) {
            if (error.isNotBlank() && result.isEmpty()) { // throw error only if result is empty
                // has error
                throw IOException(error.toString())
            }
        }

        return result
    }
}