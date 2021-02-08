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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowDraggableArea

/**
 * A common toolbar to provide close, minimise and maximize on main window
 */
@ExperimentalFoundationApi
@Composable
fun ToolBar(
    title: String = "",
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
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp),
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.End
                )
            }
        }
    }


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
            modifier = Modifier.padding(7.dp),
            imageVector = icon,
            contentDescription = ""
        )
    }
}