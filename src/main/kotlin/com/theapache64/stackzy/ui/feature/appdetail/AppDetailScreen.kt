package com.theapache64.stackzy.ui.feature.appdetail

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

    val appItemWidth = (LocalAppWindow.current.width - (CONTENT_PADDING * 2)) / GRID_SIZE

    ContentScreen(
        title = title,
        onBackClicked = onBackClicked
    ) {
        if (fatalError != null) {
            FullScreenError(
                title = R.string.any_error_title_damn_it,
                message = fatalError!!
            )
        } else {
            if (loadingMessage != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingText(
                        message = loadingMessage!!
                    )
                }
            } else if (report != null) {

                val libsUsed = mutableListOf<Library>()
                for (x in report!!.libraries.values) {
                    libsUsed.addAll(x)
                }


                LazyColumn {
                    items(
                        items = if (libsUsed.size > GRID_SIZE) {
                            libsUsed.chunked(GRID_SIZE)
                        } else {
                            listOf(libsUsed)
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