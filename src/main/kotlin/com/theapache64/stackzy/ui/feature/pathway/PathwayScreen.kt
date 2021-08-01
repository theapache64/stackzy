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
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.res.svgResource
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.ui.common.Badge
import com.theapache64.stackzy.ui.common.addHoverEffect


@Composable
fun PathwayScreen(
    viewModel: PathwayViewModel,
    onAdbSelected: () -> Unit,
    onLibrariesSelected: () -> Unit,
) {

    val loggedInAccount by viewModel.loggedInAccount.collectAsState()
    val focusedCardInfo by viewModel.focusedCardInfo.collectAsState()
    val isBrowseByLibEnabled by viewModel.isBrowseByLibEnabled.collectAsState()
    val isPlayStoreEnabled by viewModel.isPlayStoreEnabled.collectAsState()

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

            Spacer(
                modifier = Modifier.height(40.dp)
            )


            Row {
                if (isPlayStoreEnabled) {
                    PathwayCard(
                        text = "Play Store",
                        icon = svgResource("drawables/playstore.svg"),
                        onClicked = viewModel::onPlayStoreClicked,
                        onMouseEnter = viewModel::onPlayStoreCardFocused,
                        onMouseLeave = viewModel::onCardFocusLost
                    )

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )
                }

                PathwayCard(
                    text = "ADB",
                    icon = svgResource("drawables/usb.svg"),
                    onClicked = onAdbSelected,
                    onMouseEnter = viewModel::onAdbCardFocused,
                    onMouseLeave = viewModel::onCardFocusLost
                )

                if (isBrowseByLibEnabled) {
                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    PathwayCard(
                        text = "Libraries (beta)",
                        icon = svgResource("drawables/books.svg"),
                        onClicked = onLibrariesSelected,
                        onMouseEnter = viewModel::onLibrariesCardFocused,
                        onMouseLeave = viewModel::onCardFocusLost
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(40.dp)
            )

            Text(
                text = focusedCardInfo,
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
            )

        }
    }
}


@Composable
fun PathwayCard(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter,
    onMouseEnter: () -> Unit,
    onMouseLeave: () -> Unit,
    onClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.pointerMoveFilter(
            onMove = {
                onMouseEnter()
                false
            },
            onExit = {
                onMouseLeave()
                false
            }
        )
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
