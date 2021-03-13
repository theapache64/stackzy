package com.theapache64.stackzy.ui.feature.appdetail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
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
    appDetailViewModel: AppDetailViewModel,
    onLibrarySelected: (Library) -> Unit,
    onBackClicked: () -> Unit
) {
    val fatalError by appDetailViewModel.fatalError.collectAsState()
    val loadingMessage by appDetailViewModel.loadingMessage.collectAsState()
    val report by appDetailViewModel.analysisReport.collectAsState()
    val selectedTabIndex by appDetailViewModel.selectedTabIndex.collectAsState()

    val title = report?.appName ?: R.string.app_detail_title

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
        val error = fatalError
        if (error != null) {
            FullScreenError(
                image = imageResource("drawables/ic_error_code.png"),
                title = R.string.any_error_title_damn_it,
                message = error
            )
        } else {
            val _report = report
            val message = loadingMessage
            if (message != null) {
                LoadingAnimation(message)
            } else if (_report != null) {

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
                    Libraries(_report, onLibrarySelected)
                } else {
                    // More Info tab
                    MoreInfo(_report)
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
