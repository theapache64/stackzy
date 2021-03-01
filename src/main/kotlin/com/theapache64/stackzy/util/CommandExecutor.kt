package com.theapache64.stackzy.util

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
        val proc = rt.exec(commands)

        val stdInput = BufferedReader(InputStreamReader(proc.inputStream))
        val stdError = BufferedReader(InputStreamReader(proc.errorStream))

        // Read the output from the command
        var s: String?
        val result = mutableListOf<String>()
        while (stdInput.readLine().also { s = it } != null) {
            if (isLivePrint) {
                println(s)
            }
            result.add(s!!)
        }

        // Read any errors from the attempted command
        val error = StringBuilder()
        while (stdError.readLine().also { s = it } != null) {
            if (isLivePrint) {
                println(s)
            }
            error.append(s).append("\n")
        }

        if (!isSkipException) {
            if (error.isNotBlank()) {
                // has error
                throw IOException(error.toString())
            }
        }

        return result
    }
}