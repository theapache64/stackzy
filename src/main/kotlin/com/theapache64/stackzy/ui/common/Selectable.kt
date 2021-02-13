package com.theapache64.stackzy.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface Selectable {
    fun getTitle(): String
}

@Composable
fun <T : Selectable> Selectable(
    data: T,
    icon: @Composable () -> Unit,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    padding: Dp = 10.dp
) {
    var isHovered by remember { mutableStateOf(false) }
    val backgroundAlpha = if (isHovered) {
        0.2f
    } else {
        0f
    }

    Row(
        modifier = modifier
            .background(MaterialTheme.colors.secondary.copy(alpha = backgroundAlpha), RoundedCornerShape(5.dp))
            .clickable {
                onSelected(data)
            }
            .pointerMoveFilter(
                onEnter = {
                    isHovered = true
                    false
                },
                onExit = {
                    isHovered = false
                    false
                }
            ).padding(padding),
        verticalAlignment = Alignment.CenterVertically
    ) {

        icon()

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = data.getTitle(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}