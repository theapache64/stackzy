package com.theapache64.stackzy.ui.feature.appdetail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.data.local.Platform
import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.ui.common.*
import com.theapache64.stackzy.util.R

private const val GRID_SIZE = 4

@Composable
fun AppDetailScreen(
    appDetailViewModel: AppDetailViewModel,
    onLibrarySelected: (Library) -> Unit,
    onBackClicked: () -> Unit
) {
    val fatalError by appDetailViewModel.fatalError.collectAsState()
    val loadingMessage by appDetailViewModel.loadingMessage.collectAsState()
    val report by appDetailViewModel.analysisReport.collectAsState()

    val title = if (report == null || report?.appName == null) {
        R.string.app_detail_title
    } else {
        report!!.appName!!
    }

    // Calculating item width based on screen width
    val appItemWidth = (LocalAppWindow.current.width - (CONTENT_PADDING_HORIZONTAL * 2)) / GRID_SIZE

    CustomScaffold(
        title = title,
        subTitle = report?.platform?.name,
        onBackClicked = onBackClicked
    ) {
        if (fatalError != null) {
            FullScreenError(
                title = R.string.any_error_title_damn_it,
                message = fatalError!!
            )
        } else {
            if (loadingMessage != null) {
                LoadingAnimation(loadingMessage!!)
            } else if (report != null) {

                if (report!!.libraries.isEmpty()) {
                    // No libraries found
                    val platform = report!!.platform
                    if (platform is Platform.NativeKotlin || platform is Platform.NativeJava) {
                        // native platform with libs
                        FullScreenError(
                            title = "We couldn't find any libraries",
                            message = "But don't worry, we're improving our dictionary strength. Please try later",
                            image = imageResource("drawables/guy.png")
                        )
                    } else {
                        // non native platform with no libs
                        FullScreenError(
                            title = "// TODO : ",
                            message = "${report?.platform?.name} dependency analysis is yet to support",
                            image = imageResource("drawables/code.png")
                        )
                    }
                } else {


                    LazyColumn {
                        items(
                            items = if (report!!.allLibraries.size > GRID_SIZE) {
                                report!!.allLibraries.chunked(GRID_SIZE)
                            } else {
                                listOf(report!!.allLibraries)
                            }
                        ) { appSet ->
                            Row {
                                appSet.map { app ->

                                    // GridItem
                                    Selectable(
                                        modifier = Modifier.width(appItemWidth.dp),
                                        data = app,
                                        onSelected = onLibrarySelected
                                    )
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(10.dp)
                            )
                        }
                    }
                }


            }
        }


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