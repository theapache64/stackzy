package com.theapache64.stackzy.util

import com.theapache64.expekt.should
import org.junit.Test

internal class CommandExecutorTest {
    @Test
    fun `Test simple command execution`() {
        CommandExecutor.executeCommand("ls").let {
            it.split("\n").size.should.above(5)
        }
    }
}