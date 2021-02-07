package com.theapache64.stackzy.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleDown
import androidx.compose.material.icons.outlined.ArrowCircleUp
import androidx.compose.material.icons.outlined.HighlightOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowDraggableArea

@Composable
fun ToolBar(
    title: String = "",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(24.dp)
            .background(MaterialTheme.colors.secondary)
            .fillMaxWidth(),
    ) {

        // Close Icon : X
        ToolBarIcon(
            icon = Icons.Outlined.HighlightOff,
            onClick = {

            }
        )

        // Minimise Icon : ⬇
        ToolBarIcon(
            icon = Icons.Outlined.ArrowCircleDown,
            onClick = {}
        )

        // Maximize Icon : ⬆️
        ToolBarIcon(
            icon = Icons.Outlined.ArrowCircleUp,
            onClick = {}
        )

        WindowDraggableArea(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(text = title)
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