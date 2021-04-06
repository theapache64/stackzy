package com.theapache64.stackzy.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.theapache64.stackzy.ui.common.ToolBar

// Color set
val LightTheme = lightColors() // TODO :
val DarkTheme = darkColors(
    primary = R.color.TelegramBlue,
    onPrimary = Color.White,
    secondary = R.color.Elephant,
    onSecondary = Color.White,
    surface = R.color.BigStone,
    error = R.color.WildWatermelon
)

@Composable
fun StackzyTheme(
    title: String = "",
    subTitle: String = "",
    customToolbar: Boolean = false,
    isDark: Boolean = true,
    content: @Composable (ColumnScope) -> Unit
) {
    MaterialTheme(
        colors = if (isDark) DarkTheme else LightTheme,
        typography = StackzyTypography
    ) {
        Surface {
            Column {
                if (customToolbar) {
                    Column {
                        ToolBar(
                            title = title,
                            subTitle = subTitle
                        )
                        content(this)
                    }
                } else {
                    content(this)
                }
            }
        }
    }
}