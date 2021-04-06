package com.theapache64.stackzy.data.util

import java.io.File

val File.size get() = if (!exists()) 0.0 else length().toDouble()
val File.sizeInKb get() = size / 1024
val File.sizeInMb get() = sizeInKb / 1024

/**
 * To convert bytes to MB
 */
val Long.bytesToMb get() = (this / 1024) / 1024f