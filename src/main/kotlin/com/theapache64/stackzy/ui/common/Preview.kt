package com.theapache64.stackzy.ui.common

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.theapache64.stackzy.ui.theme.StackzyTheme

/**
 * To support preview feature
 */
fun Preview(
    slot: @Composable () -> Unit
) {
    Window(
        undecorated = true
    ) {
        PreviewContent {
            slot()
        }
    }
}

@Composable
private fun PreviewContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    StackzyTheme {
        Row(
            modifier = modifier.fillMaxSize()
        ) {
            content()
        }
    }
}