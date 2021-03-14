package com.theapache64.stackzy.ui.common

import androidx.compose.desktop.AppManager
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleDown
import androidx.compose.material.icons.outlined.HighlightOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowDraggableArea

/**
 * A common toolbar to provide close, minimise and maximize on main window
 */
@ExperimentalFoundationApi
@Composable
fun ToolBar(
    title: String = "",
    subTitle: String = "",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(24.dp)
            .background(MaterialTheme.colors.secondary)
            .fillMaxWidth()
    ) {

        // Close Icon : X
        ToolBarIcon(
            icon = Icons.Outlined.HighlightOff,
            onClick = {
                AppManager.focusedWindow?.close()
            }
        )

        // Minimise Icon : ⬇
        ToolBarIcon(
            icon = Icons.Outlined.ArrowCircleDown,
            onClick = {
                AppManager.focusedWindow?.minimize()
            }
        )

        WindowDraggableArea(
            modifier = Modifier
                .fillMaxSize()
            /*
            // Commenting due to https://github.com/JetBrains/compose-jb/issues/357. Feel free to uncomment once fixed.
            .combinedClickable(
                onClick = {
                    println("Clicked!")
                },
                onDoubleClick = {
                    println("Double clicked")
                }
            )*/

        ) {
            Title(title, subTitle)
        }
    }
}

@Composable
private fun Title(title: String, subTitle: String) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = getCombinedString(title, subTitle),
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp),
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.End
        )
    }
}
/**
 * To return AnnotatedString
 */
@Composable
private fun getCombinedString(title: String, subTitle: String) = with(AnnotatedString.Builder("")) {

    // Title
    pushStyle(
        SpanStyle(
            color = MaterialTheme.colors.onSurface, fontSize = 14.sp
        )
    )
    append(title)
    append("  ") // some space
    // Subtitle
    pushStyle(
        SpanStyle(
            color = MaterialTheme.colors.onSurface.copy(0.5f),
            fontSize = 12.sp
        )
    )
    append(subTitle)

    toAnnotatedString()
}

@Composable
private fun ToolBarIcon(
    icon: ImageVector,
    onClick: () -> Unit
) {
    IconButton(
        modifier = Modifier
            .size(30.dp),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.padding(3.dp),
            imageVector = icon,
            contentDescription = ""
        )
    }
}