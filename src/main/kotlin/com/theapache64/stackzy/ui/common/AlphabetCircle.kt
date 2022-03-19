package com.theapache64.stackzy.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

private const val DEFAULT_SCALE = 1f
private const val ANIMATED_SCALE = 1.2f

@Composable
fun AlphabetCircle(
    character: Char,
    color: Brush,
    modifier: Modifier = Modifier,
    isNew: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
) {
    val currentScale = if (isNew) {
        // If the library is new, we'll show a zoom-in/zoom-out animation
        var currentScaleState by remember { mutableStateOf(DEFAULT_SCALE) }

        LaunchedEffect(Unit) {
            if (isNew) {
                currentScaleState = ANIMATED_SCALE
            }
        }
        animateFloatAsState(
            currentScaleState,
            animationSpec = tween(
                durationMillis = 500
            ),
            finishedListener = {
                currentScaleState = DEFAULT_SCALE
            }
        ).value
    } else {
        DEFAULT_SCALE
    }

    Box(
        modifier = modifier
            .scale(currentScale)
            .background(color, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = character.uppercaseChar().toString(),
            style = textStyle
        )
    }

}

/*
fun main(args: Array<String>) = singleWindowApplication {
    StackzyTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AlphabetCircle(
                character = 'C',
                color = Brush.linearGradient(colors = listOf(Color.Red, Color.Green)),
                isNew = true,
                modifier = Modifier.size(60.dp)
            )
        }
    }
}*/
