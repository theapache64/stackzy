package com.theapache64.stackzy.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * To show a basic content page with title
 */
@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.padding(30.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h3
        )

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        content()
    }

}