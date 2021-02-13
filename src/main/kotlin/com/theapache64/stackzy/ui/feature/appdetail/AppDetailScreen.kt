package com.theapache64.stackzy.ui.feature.appdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
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
        title = if (report == null) {
            R.string.app_detail_title
        } else {
            report!!.appName
        },
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

                /* Column {
                     Text(
                         text = report!!.appName,
                         style = MaterialTheme.typography.h4
                     )
                 }*/

                val keys = report!!.libraries.keys.toList()
                LazyColumn {
                    items(keys) { category ->

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Text(
                            text = category,
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
                        )
                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        val libraries = report!!.libraries[category]!!

                        LazyRow {
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
                                            modifier = Modifier.width(200.dp),
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
}