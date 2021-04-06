package com.theapache64.stackzy.ui.feature

import androidx.compose.ui.unit.IntSize
import com.arkivanov.decompose.extensions.compose.jetbrains.rememberRootComponent
import com.theapache64.cyclone.core.Activity
import com.theapache64.cyclone.core.Intent
import com.theapache64.stackzy.App
import com.theapache64.stackzy.ui.navigation.NavHostComponent
import com.theapache64.stackzy.ui.theme.StackzyTheme
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import androidx.compose.desktop.Window as setContent


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

        setContent(
            title = "${App.appArgs.appName} (${App.appArgs.version})",
            icon = getAppIcon(),
            undecorated = App.CUSTOM_TOOLBAR,
            size = IntSize(1024, 600),
        ) {
            StackzyTheme(
                title = App.appArgs.appName,
                subTitle = "(${App.appArgs.version})",
                customToolbar = App.CUSTOM_TOOLBAR
            ) {
                // Igniting navigation
                rememberRootComponent(factory = ::NavHostComponent)
                    .render()
            }
        }

    }

    /**
     * To get app icon for toolbar and system tray
     */
    private fun getAppIcon(): BufferedImage {

        // Retrieving image
        val resourceFile = MainActivity::class.java.classLoader.getResourceAsStream(
            "drawables/launcher_icons/linux.png"
        )
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
}