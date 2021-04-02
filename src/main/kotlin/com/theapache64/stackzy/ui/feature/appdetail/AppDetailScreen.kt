package com.theapache64.stackzy.ui.feature.appdetail

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.svgResource
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.ui.common.Badge
import com.theapache64.stackzy.ui.common.CustomScaffold
import com.theapache64.stackzy.ui.common.FullScreenError
import com.theapache64.stackzy.ui.common.LoadingAnimation
import com.theapache64.stackzy.util.R


@Composable
fun AppDetailScreen(
    viewModel: AppDetailViewModel,
    onLibrarySelected: (Library) -> Unit,
    onBackClicked: () -> Unit
) {
    val fatalError by viewModel.fatalError.collectAsState()
    val loadingMessage by viewModel.loadingMessage.collectAsState()
    val report by viewModel.analysisReport.collectAsState()
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsState()

    val title = report?.appName ?: R.string.app_detail_title

    CustomScaffold(
        title = title,
        subTitle = report?.platform?.name,
        onBackClicked = onBackClicked,
        bottomGradient = loadingMessage == null, // hide when loading message shows
        topRightSlot = {
            if (loadingMessage == null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    report?.let {

                        // Badge
                        Badge("APK SIZE: ${it.apkSizeInMb} MB")

                        Spacer(
                            modifier = Modifier.width(5.dp)
                        )

                        // Launch app in play-store icon
                        PlayStoreIcon {
                            viewModel.onPlayStoreIconClicked()
                        }

                        CodeIcon {
                            viewModel.onCodeIconClicked()
                        }
                    }
                }
            }
        }
    ) {

        // Using backing property to prevent double bang usage.
        @Suppress("UnnecessaryVariable")
        val roFatalError = fatalError
        if (roFatalError != null) {
            FullScreenError(
                image = imageResource("drawables/ic_error_code.png"),
                title = R.string.any_error_title_damn_it,
                message = roFatalError
            )
        } else {

            @Suppress("UnnecessaryVariable")
            val roReport = report
            @Suppress("UnnecessaryVariable")
            val roLoadingMsg = loadingMessage

            if (roLoadingMsg != null) {
                LoadingAnimation(roLoadingMsg)
            } else if (roReport != null) {

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column {
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
                                        viewModel.onTabClicked(index)
                                    },
                                    text = { Text(text = title) }
                                )
                            }
                        }

                        if (selectedTabIndex == 0) {
                            // Libraries Tab
                            Libraries(roReport, onLibrarySelected)
                        } else {
                            // More Info tab
                            MoreInfo(roReport)
                        }
                    }
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
private fun CodeIcon(onClicked: () -> Unit) {
    IconButton(
        onClick = onClicked
    ) {
        Icon(
            imageVector = Icons.Outlined.Code,
            contentDescription = "Browse Code"
        )
    }
}
