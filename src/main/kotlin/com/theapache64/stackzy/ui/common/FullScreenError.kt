package com.theapache64.stackzy.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp

/**
 * To show full screen error with centered title and message
 */
@Composable
fun FullScreenError(
    title: String,
    message: String,
    image: ImageBitmap? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (image != null) {
            // Image
            Image(
                bitmap = image,
                modifier = Modifier.width(300.dp),
                contentDescription = ""
            )
        }

        /*Space*/
        Spacer(
            modifier = Modifier.height(30.dp)
        )

        /*Title*/
        Text(
            text = title,
            style = MaterialTheme.typography.h4
        )

        /*Space*/
        Spacer(
            modifier = Modifier.height(10.dp)
        )

        /*Message*/
        Text(
            text = message,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
        )
    }
}