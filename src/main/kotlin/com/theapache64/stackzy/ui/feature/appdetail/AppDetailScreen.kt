package com.theapache64.stackzy.ui.feature.appdetail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.svgResource
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.ui.common.CustomScaffold
import com.theapache64.stackzy.ui.common.FullScreenError
import com.theapache64.stackzy.ui.common.LoadingText
import com.theapache64.stackzy.util.R


@Composable
fun AppDetailScreen(
    appDetailViewModel: AppDetailViewModel,
    onLibrarySelected: (Library) -> Unit,
    onBackClicked: () -> Unit
) {
    val fatalError by appDetailViewModel.fatalError.collectAsState()
    val loadingMessage by appDetailViewModel.loadingMessage.collectAsState()
    val report by appDetailViewModel.analysisReport.collectAsState()
    val selectedTabIndex by appDetailViewModel.selectedTabIndex.collectAsState()

    val title = if (report == null || report?.appName == null) {
        R.string.app_detail_title
    } else {
        report!!.appName!!
    }


    CustomScaffold(
        title = title,
        subTitle = report?.platform?.name,
        onBackClicked = {
            appDetailViewModel.onBackPressed() // to cancel on going works
            onBackClicked()
        },
        topRightSlot = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                report?.let {

                    // Badge
                    Badge("APK SIZE: ${it.apkSizeInMb} MB")

                    // Launch app in play-store icon
                    PlayStoreIcon {
                        appDetailViewModel.onPlayStoreIconClicked()
                    }
                }
            }
        }
    ) {
        if (fatalError != null) {
            FullScreenError(
                image = imageResource("drawables/code.png"),
                title = R.string.any_error_title_damn_it,
                message = fatalError!!
            )
        } else {
            if (loadingMessage != null) {
                LoadingAnimation(loadingMessage!!)
            } else if (report != null) {

                // Decompile and analysis done
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    // Tabs
                    AppDetailViewModel.TABS.forEachIndexed { index, title ->
                        Tab(
                            selected = index == selectedTabIndex,
                            onClick = {
                                appDetailViewModel.onTabClicked(index)
                            },
                            text = { Text(text = title) }
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                if (selectedTabIndex == 0) {
                    // Libraries Tab
                    Libraries(report!!, onLibrarySelected)
                } else {
                    // More Info tab
                    MoreInfo(report!!)
                }

            }
        }
    }
}

@Composable
private fun PlayStoreIcon(onClicked: () -> Unit) {
    IconButton(
        onClick = onClicked
    ) {
        Icon(
            painter = svgResource("drawables/playstore.svg"),
            contentDescription = "open play store",
            tint = MaterialTheme.colors.onSurface
        )
    }
}


@Composable
private fun LoadingAnimation(loadingMessage: String) {

    var enabled by remember { mutableStateOf(true) }

    val alpha = if (enabled) {
        0f
    } else {
        180f
    }

    val animatedValue by animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(200),
        finishedListener = {
            enabled = !enabled
        }
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .rotate(animatedValue)
                .align(Alignment.Center)
                .size(50.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.primary),
            bitmap = imageResource("drawables/loading.png"),
            contentDescription = ""
        )

        LoadingText(
            modifier = Modifier.align(Alignment.BottomCenter),
            message = loadingMessage
        )
    }
}


@Composable
private fun Badge(
    title: String
) {
    Text(
        text = title,
        modifier = Modifier
            .background(MaterialTheme.colors.secondary, RoundedCornerShape(5.dp))
            .padding(5.dp),
        style = MaterialTheme.typography.caption,
    )
}
