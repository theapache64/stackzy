package com.theapache64.stackzy.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun main() {
    Preview {
        AlphabetCircle(
            text = "ANDROID",
            modifier = Modifier
                .padding(10.dp)
                .size(60.dp)
        )
    }
}


@Composable
fun AlphabetCircle(
    text: String,
    modifier: Modifier = Modifier
) {


    Box(
        modifier = modifier
            .background(Color.Red, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.first().toString(),
            style = MaterialTheme.typography.h5
        )
    }
}