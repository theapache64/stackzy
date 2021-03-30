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
import com.theapache64.stackzy.ui.util.Preview

// Preview
fun main() {
    Preview {
        LoadingAnimation("Test")
    }
}


/**
 * To show a rotating icon at the center and blinking text at the bottom of the screen
 */
@Composable
fun LoadingAnimation(message: String) {

    var isRotated by remember { mutableStateOf(false) }

    val targetRotation = if (isRotated) {
        0f
    } else {
        90f
    }

    val animatedRotation by animateFloatAsState(
        targetValue = targetRotation,
        animationSpec = tween(200),
        finishedListener = {
            isRotated = !isRotated
        }
    )


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .rotate(animatedRotation)
                .align(Alignment.Center)
                .size(50.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
            bitmap = imageResource("drawables/loading.png"),
            contentDescription = ""
        )

        LoadingText(
            modifier = Modifier.align(Alignment.BottomCenter),
            message = message
        )
    }

    LaunchedEffect(Unit){
        // Ignite the animation
        isRotated = !isRotated
    }
}
