package com.theapache64.stackzy.ui.feature.pathway

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.svgResource
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.ui.theme.R
import com.theapache64.stackzy.ui.util.Preview

fun main(args: Array<String>) {
    Preview {
        PathwayScreen()
    }
}

@Composable
fun PathwayScreen() {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        PathwayCard(
            text = "Play Store",
            icon = svgResource("drawables/playstore.svg"),
            onClicked = {

            }
        )
    }
}

@Composable
fun PathwayCard(
    text: String,
    icon: Painter,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .width(300.dp)
            .background(R.color.BigStone)
    ) {

    }
}
