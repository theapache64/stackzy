package com.theapache64.stackzy.ui.feature

import com.arkivanov.decompose.extensions.compose.jetbrains.rootComponent
import com.theapache64.cyclone.core.Activity
import com.theapache64.cyclone.core.Intent
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
            undecorated = true
        ) {
            StackzyTheme {
                rootComponent(factory = ::NavHostComponent)
                    .render()
            }
        }

    }
}