package com.theapache64.stackzy.util

import androidx.compose.desktop.Window
import androidx.compose.runtime.Composable
import com.theapache64.cyclone.core.Activity

fun Activity.setContent(content: @Composable () -> Unit) {
    Window {
        content()
    }
}