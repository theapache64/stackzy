package com.theapache64.stackzy.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.theapache64.stackzy.ui.common.ToolBar

// Colors
private val TelegramBlue = Color(48, 163, 230)
private val WoodSmoke = Color(24, 25, 29)
private val OuterSpace = Color(53, 60, 67)
private val BrightGray = Color(58, 64, 71)

// Color set
val LightTheme = lightColors()
val DarkTheme = darkColors(
    primary = TelegramBlue,
    onPrimary = Color.White,

    secondary = BrightGray,
    onSecondary = Color.White,

    surface = WoodSmoke,
)

@Composable
fun StackzyTheme(
    title: String = "",
    displayToolbar: Boolean = true,
    isDark: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (isDark) DarkTheme else LightTheme,
        typography = StackzyTypography
    ) {
        Surface {
            Column {
                if (displayToolbar) {
                    Column {
                        ToolBar(
                            title = title
                        )
                        content()
                    }
                } else {
                    content()
                }
            }
        }
    }
}