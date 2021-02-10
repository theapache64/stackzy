package com.theapache64.stackzy.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

fun runBlockingUnitTest(block: suspend (scope: CoroutineScope) -> Unit) = runBlocking {
    block(this)
    Unit
}