package com.theapache64.stackzy.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.singleWindowApplication
import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.model.AlphabetCircle
import com.theapache64.stackzy.model.LibraryWrapper
import com.theapache64.stackzy.ui.theme.StackzyTheme
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
import kotlin.system.exitProcess


/**
 * Once this applied, when you hover the mouse over the item, it's background color will be changed.
 */
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

// Preview
fun main() = application {

    Window(
        onCloseRequest = {
            exitProcess(0)
        }
    ) {
        StackzyTheme {
            Selectable(
                data = object : AlphabetCircle() {
                    override fun getTitle() = "WhatsApp"
                    override fun getSubtitle() = "v1.0.0"
                    override fun imageUrl() =
                        "https://play-lh.googleusercontent.com/X64En0aW6jkvDnd5kr16u-YuUsoJ1W2cBzJab3CQ5lObLeQ3T61DpB7AwIoZ7uqgCn4"
                },
                onSelected = {

                }
            )
        }
    }
}

fun main(args: Array<String>) = singleWindowApplication {
    StackzyTheme {
        val dummyLib = LibraryWrapper(
            Library(
                category = "Category 1",
                id = 0,
                name = "My Lib",
                packageName = "com.something",
                replacementPackage = null,
                website = ""
            ),
            null
        )
        Selectable(
            data = dummyLib,
            onSelected = {

            }
        )
    }
}

@Composable
fun <T : AlphabetCircle> Selectable(
    data: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = false,
    padding: Dp = if (isCompact) {
        5.dp
    } else {
        10.dp
    }
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
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
            AlphabetCircle(data = data, isCompact = isCompact)
        } else {
            // Show alphabet then image
            KamelImage(
                resource = lazyPainterResource(data.imageUrl()!!),
                contentScale = ContentScale.FillBounds,
                contentDescription = "app logo",
                onLoading = {
                    AlphabetCircle(data = data, isCompact = isCompact)
                },
                onFailure = {
                    AlphabetCircle(data = data, isCompact = isCompact)
                },

                modifier = Modifier.size(if (isCompact) 30.dp else 60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary) // outer blue
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.secondary) // gap
                    .padding(4.dp)
                    .clip(CircleShape) // logo
            )
        }

        Spacer(
            modifier = Modifier.width(10.dp)
        )

        Column {

            // App Title
            SelectionContainer {
                Text(
                    text = data.getTitle(),
                    maxLines = 1,
                    style = if (isCompact) MaterialTheme.typography.body2 else MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Subtitle
            SelectionContainer {
                Text(
                    text = data.getSubtitle(),
                    maxLines = 1,
                    style = if (isCompact) {
                        MaterialTheme.typography.body2.copy(fontSize = 10.sp)
                    } else {
                        MaterialTheme.typography.body2
                    },
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                )
            }

            // Subtitle 2
            data.getSubtitle2()?.let { subTitle2 ->
                SelectionContainer {
                    Text(
                        text = subTitle2,
                        maxLines = 1,
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                    )
                }
            }
        }

        if (data.isNew()) {
            // TODO: Find a good position
            Text(
                text = "✨",
                modifier = Modifier.padding(10.dp),
            )
        }
    }
}

@Composable
fun LabelNew() {

}

@Composable
private fun <T : AlphabetCircle> AlphabetCircle(data: T, isCompact: Boolean) {
    AlphabetCircle(
        character = data.getAlphabet(),
        color = data.getGradientColor(),
        modifier = Modifier.size(if (isCompact) 30.dp else 60.dp),
        isNew = data.isNew(),
        textStyle = if (isCompact) {
            MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
        } else {
            MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
        }
    )
}