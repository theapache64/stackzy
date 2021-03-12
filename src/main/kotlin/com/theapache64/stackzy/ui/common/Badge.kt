package com.theapache64.stackzy.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Badge(
    title: String,
    padding: Dp = 5.dp
) {
    Text(
        text = title,
        modifier = Modifier
            .background(MaterialTheme.colors.secondary, RoundedCornerShape(5.dp))
            .padding(padding),
        style = MaterialTheme.typography.caption,
    )
}
