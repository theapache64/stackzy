package com.theapache64.stackzy.ui.feature

import androidx.compose.ui.unit.IntSize
import com.arkivanov.decompose.extensions.compose.jetbrains.rootComponent
import com.theapache64.cyclone.core.Activity
import com.theapache64.cyclone.core.Intent
import com.theapache64.stackzy.App
import com.theapache64.stackzy.ui.navigation.NavHostComponent
import com.theapache64.stackzy.ui.theme.StackzyTheme
import com.theapache64.stackzy.util.R
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
            title = R.string.app_name,
            undecorated = App.CUSTOM_TOOLBAR,
            size = IntSize(1024, 600),
        ) {
            StackzyTheme(
                title = R.string.app_name,
                customToolbar = App.CUSTOM_TOOLBAR
            ) {
                // Igniting navigation
                rootComponent(factory = ::NavHostComponent)
                    .render()
            }
        }

    }
}