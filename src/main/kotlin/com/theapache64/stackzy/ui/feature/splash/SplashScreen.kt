package com.theapache64.stackzy.ui.feature.splash

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.ui.common.LoadingText
import com.theapache64.stackzy.ui.common.Logo

@ExperimentalFoundationApi
@Composable
fun SplashScreen(splashScreenViewModel: SplashScreenViewModel) {
    println("Splash : $splashScreenViewModel")
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Logo(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
        )

        LoadingText(
            message = "Syncing...",
            modifier = Modifier
                .padding(bottom = 30.dp)
                .align(Alignment.BottomCenter)

        )
    }
}

