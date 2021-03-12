package com.theapache64.stackzy.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.util.ColorUtil
import io.kamel.image.KamelImage
import io.kamel.image.lazyImageResource

abstract class AlphabetCircle {

    abstract fun getTitle(): String
    abstract fun getSubtitle(): String
    open fun getSubtitle2(): String? = null
    abstract fun imageUrl(): String?
    open fun getAlphabet() = getTitle().first()

    val randomColor = ColorUtil.getRandomColor()
    val brighterColor = ColorUtil.getBrightenedColor(randomColor)
    val bgColor = Brush.horizontalGradient(
        colors = listOf(randomColor, brighterColor)
    )

    fun getGradientColor(): Brush = bgColor
}

@Composable
fun Modifier.addHoverEffect(
    onClicked: () -> Unit,
    normalColor: Color = MaterialTheme.colors.secondary,
    normalAlpha: Float = 0f,
    hoverAlpha: Float = 0.8f,
    cornerRadius: Dp = 5.dp
): Modifier {
    var isHovered by remember { mutableStateOf(false) }
    val backgroundAlpha = if (isHovered) {
        hoverAlpha
    } else {
        normalAlpha
    }

    return this
        .background(normalColor.copy(alpha = backgroundAlpha), RoundedCornerShape(cornerRadius))
        .clickable {
            onClicked()
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
        )
}

@Composable
fun <T : AlphabetCircle> Selectable(
    data: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    padding: Dp = 10.dp
) {

    Row(
        modifier = modifier
            .addHoverEffect(
                onClicked = {
                    onSelected(data)
                }
            )
            .padding(padding),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (data.imageUrl() == null) {
            // Show only alphabet
            AlphabetCircle(data)
        } else {
            // Show alphabet then image
            KamelImage(
                resource = lazyImageResource(data.imageUrl()!!),
                contentScale = ContentScale.Inside,
                contentDescription = "app logo",
                onLoading = {
                    AlphabetCircle(data)
                },
                onFailure = {
                    AlphabetCircle(data)
                },
                modifier = Modifier.size(60.dp).clip(CircleShape)
            )
        }



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

            Text(
                text = data.getSubtitle(),
                maxLines = 1,
                style = MaterialTheme.typography.body2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
            )

            data.getSubtitle2()?.let { subTitle2 ->
                Text(
                    text = subTitle2,
                    maxLines = 1,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Composable
private fun <T : AlphabetCircle> AlphabetCircle(data: T) {
    AlphabetCircle(
        data.getAlphabet(),
        data.getGradientColor(),
        modifier = Modifier.size(60.dp)
    )
}