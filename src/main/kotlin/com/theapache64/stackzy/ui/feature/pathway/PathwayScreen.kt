package com.theapache64.stackzy.ui.feature.pathway

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.svgResource
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.ui.common.Badge
import com.theapache64.stackzy.ui.common.addHoverEffect


@Composable
fun PathwayScreen(
    viewModel: PathwayViewModel,
    onAdbSelected: () -> Unit
) {

    val loggedInAccount by viewModel.loggedInAccount.collectAsState()

    Box {
        // Logout
        loggedInAccount?.let { account ->
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "(${account.username})",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                Badge(
                    title = "Logout",
                    modifier = Modifier.clickable {
                        viewModel.onLogoutClicked()
                    }
                )
            }
        }

        // Content
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
                PathwayCard(
                    text = "Play Store",
                    icon = svgResource("drawables/playstore.svg"),
                    onClicked = viewModel::onPlayStoreClicked
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )

                PathwayCard(
                    text = "ADB",
                    icon = svgResource("drawables/usb.svg"),
                    onClicked = onAdbSelected
                )
            }
        }
    }
}


@Composable
fun PathwayCard(
    text: String,
    icon: Painter,
    onClicked: () -> Unit,
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
