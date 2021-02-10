package com.theapache64.stackzy.ui.common

import androidx.compose.foundation.Image
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.imageFromResource
import com.theapache64.stackzy.util.R

@Composable
fun Logo(
    modifier: Modifier = Modifier
) {

    Image(
        contentDescription = R.string.logo,
        bitmap = imageFromResource("drawables/stones.png"),
        colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
        modifier = modifier,
    )
}