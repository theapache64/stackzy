package com.theapache64.stackzy.ui.feature.pathway

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.svgResource
import androidx.compose.ui.unit.dp
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
            .width(200.dp)
            .fillMaxHeight(0.5f)
            .background(MaterialTheme.colors.secondary, RoundedCornerShape(10.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /*Icon*/
        Image(
            painter = icon,
            modifier = Modifier.size(80.dp),
            contentDescription = "play store"
        )

        Text(
            text = text
        )
    }
}
