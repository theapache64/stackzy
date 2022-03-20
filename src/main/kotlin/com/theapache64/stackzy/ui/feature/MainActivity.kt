package com.theapache64.stackzy.ui.feature

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.theapache64.cyclone.core.Activity
import com.theapache64.cyclone.core.Intent
import com.theapache64.stackzy.App
import com.theapache64.stackzy.ui.navigation.NavHostComponent
import com.theapache64.stackzy.ui.theme.R
import com.theapache64.stackzy.ui.theme.StackzyTheme
import com.theapache64.stackzy.util.OSType
import com.theapache64.stackzy.util.OsCheck
import java.awt.Taskbar
import java.awt.Toolkit
import java.awt.image.BufferedImage
import javax.imageio.ImageIO


class MainActivity : Activity() {
    companion object {
        fun getStartIntent(): Intent {
            return Intent(MainActivity::class).apply {
                // data goes here
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        try {
            /*
             *TODO : Temp fix for https://github.com/theapache64/stackzy/issues/72
             *  Should be updated once resolved :
             */
            Taskbar.getTaskbar().iconImage = getAppIcon()
        } catch (e: UnsupportedOperationException) {
            e.printStackTrace()
        }

        val lifecycle = LifecycleRegistry()
        val root = NavHostComponent(DefaultComponentContext(lifecycle))

        application {

            val title = "${App.appArgs.appName} (${App.appArgs.version})"

            val screenSize = Toolkit.getDefaultToolkit().screenSize

            Window(
                onCloseRequest = ::exitApplication,
                title = if (OsCheck.operatingSystemType != OSType.MacOS) {
                    title
                } else {
                    ""
                },
                icon = painterResource(R.drawables.appIcon),
                state = rememberWindowState(
                    width = (screenSize.width * 0.9).dp,
                    height = (screenSize.height * 0.8).dp
                ),
                onKeyEvent = {
                    if (it.type == KeyEventType.KeyUp && it.key.keyCode == Key.Escape.keyCode) {
                        root.onEscapePressed()
                        true
                    } else {
                        false
                    }
                }
            ) {

                if (OsCheck.operatingSystemType == OSType.MacOS) {
                    window.rootPane.apply {
                        rootPane.putClientProperty("apple.awt.fullWindowContent", true)
                        rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
                    }
                }

                StackzyTheme {
                    if (OsCheck.operatingSystemType == OSType.MacOS) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = title, style = MaterialTheme.typography.subtitle1)

                            // Igniting navigation
                            root.render()
                        }
                    } else {
                        // Igniting navigation
                        root.render()
                    }
                }
            }
        }


    }
}


/**
 * To get app icon for toolbar and system tray
 */
private fun getAppIcon(): BufferedImage {

    // Retrieving image
    val resourceFile = MainActivity::class.java.classLoader.getResourceAsStream(R.drawables.appIcon)
    val imageInput = ImageIO.read(resourceFile)

    val newImage = BufferedImage(
        imageInput.width,
        imageInput.height,
        BufferedImage.TYPE_INT_ARGB
    )

    // Drawing
    val canvas = newImage.createGraphics()
    canvas.drawImage(imageInput, 0, 0, null)
    canvas.dispose()

    return newImage
}
