package com.theapache64.stackzy.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import com.theapache64.stackzy.ui.util.Preview

/*
fun main() {
    Preview {
        LazyRow {
            items(count = 10) {

            }
        }
    }
}
*/


@Composable
fun AlphabetCircle(
    character: Char,
    color: Brush,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .background(color, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = character.toUpperCase().toString(),
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
        )
    }
}
