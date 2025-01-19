package com.theapache64.stackzy.util

import com.theapache64.stackzy.model.AndroidDeviceWrapper

sealed interface ApkSource {
    class Adb(val value: AndroidDeviceWrapper) : ApkSource
    data object PlayStore : ApkSource
}