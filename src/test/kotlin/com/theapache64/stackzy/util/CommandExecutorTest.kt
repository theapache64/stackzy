package com.theapache64.stackzy.util

import com.theapache64.expekt.should
import org.junit.Test

internal class CommandExecutorTest {
    @Test
    fun `Test simple command execution`() {
        val command = "java -jar 'C:\\Users\\theap\\Desktop\\stackzy\\apk-tool.jar' d 'C:\\Users\\theap\\AppData\\Local\\Temp\\4613591304202884654.apk' -o 'C:\\Users\\theap\\AppData\\Local\\Temp\\5314682004081986574' -f"
        CommandExecutor.executeCommand(command, isLivePrint = true, isSkipException = true).let {
            it.split("\n").size.should.above(2)
        }
    }
}