package com.theapache64.stackzy.ui.feature.pathway

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.svgResource
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.ui.common.addHoverEffect
import com.theapache64.stackzy.ui.util.Preview

fun main(args: Array<String>) {
    Preview {
        PathwayScreen()
    }
}

@Composable
fun PathwayScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        /*Title*/
        Text(
            text = "Choose pathway",
            style = MaterialTheme.typography.h3
        )

        Text(
            text = "find APK using",
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        )

        Spacer(
            modifier = Modifier.height(40.dp)
        )


        Row {
            PathwayCards(
                onAdbClicked = {

                },
                onPlayStoreClicked = {

                }
            )
        }
    }
}

@Composable
private fun PathwayCards(
    onPlayStoreClicked: () -> Unit,
    onAdbClicked: () -> Unit
) {
    PathwayCard(
        text = "Play Store",
        icon = svgResource("drawables/playstore.svg"),
        onClicked = onPlayStoreClicked
    )

    Spacer(
        modifier = Modifier.width(10.dp)
    )

    PathwayCard(
        text = "ADB",
        icon = svgResource("drawables/usb.svg"),
        onClicked = onAdbClicked
    )
}

@Composable
fun PathwayCard(
    text: String,
    icon: Painter,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /*Icon*/
        Image(
            painter = icon,
            modifier = Modifier
                .addHoverEffect(
                    onClicked = onClicked,
                    normalAlpha = 0.3f,
                    cornerRadius = 10.dp
                )
                .padding(
                    horizontal = 80.dp,
                    vertical = 100.dp
                )
                .size(50.dp)
                .fillMaxHeight(0.5f),
            contentDescription = "play store",
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onSecondary)
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
        )
    }
}
