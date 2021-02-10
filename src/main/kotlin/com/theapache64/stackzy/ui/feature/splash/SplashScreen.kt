package com.theapache64.stackzy.ui.feature.splash

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.util.R

@ExperimentalFoundationApi
@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Logo(
            modifier = Modifier.align(Alignment.Center)
        )


        LoadingText(
            message = "Sending coordinates",
            modifier = Modifier
                .padding(bottom = 30.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun LoadingText(
    message: String,
    modifier: Modifier = Modifier
) {
    val msgState by mutableStateOf(message)

    Text(
        text = msgState,
        modifier = modifier
    )
    
}


@Composable
private fun Logo(
    modifier: Modifier = Modifier
) {
    Image(
        contentDescription = R.string.logo,
        bitmap = imageFromResource("drawables/stones.png"),
        colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
        modifier = modifier.size(100.dp)
    )
}