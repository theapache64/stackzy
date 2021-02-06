package com.theapache64.stackzy.feature.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.value.observe
import com.theapache64.cyclone.core.Activity
import com.theapache64.cyclone.core.Intent
import com.theapache64.stackzy.util.setContent

class SplashActivity : Activity() {
    companion object {
        fun getStartIntent(): Intent {
            return Intent(SplashActivity::class).apply {
                // data goes here
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        setContent {
            MaterialTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(
                            text = "I am a desktop app"
                        )
                    }
                }
            }
        }

    }
}