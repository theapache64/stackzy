package com.theapache64.stackzy.ui.feature.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SplashScreen() {
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