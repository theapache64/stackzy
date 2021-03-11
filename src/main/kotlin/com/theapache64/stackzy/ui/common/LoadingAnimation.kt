package com.theapache64.stackzy.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp

@Composable
fun LoadingAnimation(loadingMessage: String) {

    var enabled by remember { mutableStateOf(true) }

    val alpha = if (enabled) {
        0f
    } else {
        180f
    }

    val animatedValue by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(200),
        finishedListener = {
            enabled = !enabled
        }
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .rotate(animatedValue)
                .align(Alignment.Center)
                .size(50.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
            bitmap = imageResource("drawables/loading.png"),
            contentDescription = ""
        )

        LoadingText(
            modifier = Modifier.align(Alignment.BottomCenter),
            message = loadingMessage
        )
    }
}
