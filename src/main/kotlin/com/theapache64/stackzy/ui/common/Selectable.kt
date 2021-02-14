package com.theapache64.stackzy.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.util.ColorUtil

abstract class AlphabetCircle {

    abstract fun getTitle(): String
    abstract fun getSubtitle(): String
    open fun getAlphabet() = getTitle().first()

    val randomColor = ColorUtil.getRandomColor()
    val brighterColor = ColorUtil.getBrightenedColor(randomColor)
    val bgColor = Brush.horizontalGradient(
        colors = listOf(randomColor, brighterColor)
    )

    fun getGradientColor(): Brush = bgColor
}

@Composable
fun <T : AlphabetCircle> Selectable(
    data: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    padding: Dp = 10.dp
) {
    var isHovered by remember { mutableStateOf(false) }
    val backgroundAlpha = if (isHovered) {
        0.8f
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

        AlphabetCircle(
            data.getAlphabet(),
            data.getGradientColor(),
            modifier = Modifier.size(60.dp)
        )

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Column {

            Text(
                text = data.getTitle(),
                maxLines = 1,
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Text(
                text = data.getSubtitle(),
                maxLines = 1,
                style = MaterialTheme.typography.body2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}