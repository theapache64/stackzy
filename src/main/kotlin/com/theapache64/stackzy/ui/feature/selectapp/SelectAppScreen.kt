package com.theapache64.stackzy.ui.feature.selectapp

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.ui.common.*
import com.theapache64.stackzy.util.R
import com.theapache64.stackzy.util.calladapter.flow.Resource

private const val GRID_SIZE = 3

/**
 * To select an application from the selected device
 */
@Composable
fun SelectAppScreen(
    selectAppViewModel: SelectAppViewModel,
    onBackClicked: () -> Unit,
    onAppSelected: (AndroidApp) -> Unit
) {

    val searchKeyword by selectAppViewModel.searchKeyword.collectAsState()
    val appsResponse by selectAppViewModel.apps.collectAsState()

    // Calculating item width based on screen width
    val appItemWidth = (LocalAppWindow.current.width - (CONTENT_PADDING_HORIZONTAL * 2)) / GRID_SIZE

    CustomScaffold(
        title = R.string.select_app_title,
        onBackClicked = onBackClicked,
        topRightSlot = {

            // SearchBox
            OutlinedTextField(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = ""
                    )
                },
                singleLine = true,
                value = searchKeyword,
                label = {
                    Text(
                        text = R.string.select_app_label_search,
                    )
                },
                onValueChange = {
                    selectAppViewModel.onSearchKeywordChanged(it)
                },
                modifier = Modifier
                    .width(300.dp)
            )
        }
    ) {

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        when (appsResponse) {
            is Resource.Loading -> {
                val message = (appsResponse as Resource.Loading<List<AndroidApp>>).message ?: ""
                LoadingAnimation(message)
            }
            is Resource.Error -> {
                Box {
                    ErrorSnackBar(
                        (appsResponse as Resource.Error<List<AndroidApp>>).errorData
                    )
                }
            }
            is Resource.Success -> {
                val apps = (appsResponse as Resource.Success<List<AndroidApp>>).data
                if (apps.isNotEmpty()) {
                    // Grid
                    LazyColumn {
                        items(
                            items = if (apps.size > GRID_SIZE) {
                                apps.chunked(GRID_SIZE)
                            } else {
                                listOf(apps)
                            }
                        ) { appSet ->
                            Row {
                                appSet.map { app ->

                                    // GridItem
                                    Selectable(
                                        modifier = Modifier.width(appItemWidth.dp),
                                        data = app,
                                        onSelected = onAppSelected
                                    )
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(10.dp)
                            )
                        }
                    }
                } else {
                    // No app found
                    FullScreenError(
                        title = "App not found",
                        message = "Couldn't find any app with $searchKeyword",
                        image = imageResource("drawables/woman_desk.png"),
                        action = {
                            Button(
                                onClick = {
                                    selectAppViewModel.onOpenMarketClicked()
                                },
                            ) {
                                Text(text = R.string.app_detail_action_open_market)
                            }
                        }
                    )
                }
            }
            null -> TODO()
        }
    }
}