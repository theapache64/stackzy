package com.theapache64.stackzy.ui.feature.appdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.ui.common.ContentScreen
import com.theapache64.stackzy.ui.common.FullScreenError
import com.theapache64.stackzy.ui.common.LoadingText
import com.theapache64.stackzy.ui.common.Selectable
import com.theapache64.stackzy.ui.feature.selectapp.AppIcon
import com.theapache64.stackzy.util.R

private const val GRID_SIZE = 3

@Composable
fun AppDetailScreen(
    appDetailViewModel: AppDetailViewModel,
    onLibrarySelected: (Library) -> Unit,
    onBackClicked: () -> Unit
) {
    val fatalError by appDetailViewModel.fatalError.collectAsState()
    val loadingMessage by appDetailViewModel.loadingMessage.collectAsState()
    val report by appDetailViewModel.analysisReport.collectAsState()

    ContentScreen(
        title = R.string.app_detail_title,
        onBackClicked = onBackClicked
    ) {
        if (fatalError != null) {
            FullScreenError(
                title = "Damn It!",
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

                Column {
                    Text(text = report!!.appName)
                }

                report!!.libraries.entries.forEach { (category: String, libraries: List<Library>) ->
                    Text(text = category)

                    LazyColumn {
                        items(
                            items = if (libraries.size > GRID_SIZE) {
                                libraries.chunked(GRID_SIZE)
                            } else {
                                listOf(libraries)
                            }
                        ) { appSet ->
                            Row {
                                appSet.map { app ->

                                    // GridItem
                                    Selectable(
                                        data = app,
                                        icon = {
                                            AppIcon()
                                        },
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