package com.theapache64.stackzy.ui.util

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.theapache64.stackzy.ui.theme.StackzyTheme

/**
 * To support instant preview (replacement for android's @Preview annotation)
 */
fun Preview(
    modifier: Modifier = Modifier,
    undecorated: Boolean = true,
    content: @Composable () -> Unit
) {
    Window(
        undecorated = undecorated
    ) {
        StackzyTheme(
            displayToolbar = undecorated
        ) {
            Row(
                modifier = modifier.fillMaxSize()
            ) {
                content()
            }
        }
    }
}
