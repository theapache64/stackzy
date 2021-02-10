package com.theapache64.stackzy.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

@Composable
fun LoadingText(
    message: String,
    modifier: Modifier = Modifier
) {
    var enabled by remember { mutableStateOf(true) }

    val alpha = if (enabled) {
        1f
    } else {
        0.2f
    }
    val animatedAlpha by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(200),
        finishedListener = {
            enabled = !enabled
        }
    )

    Text(
        text = message,
        modifier = modifier.alpha(animatedAlpha)
    )
}