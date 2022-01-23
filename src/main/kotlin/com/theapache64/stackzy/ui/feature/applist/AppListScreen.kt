package com.theapache64.stackzy.ui.feature.applist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.theapache64.stackzy.model.AndroidAppWrapper
import com.theapache64.stackzy.ui.common.CustomScaffold
import com.theapache64.stackzy.ui.common.ErrorSnackBar
import com.theapache64.stackzy.ui.common.FullScreenError
import com.theapache64.stackzy.ui.common.Selectable
import com.theapache64.stackzy.ui.common.loading.LoadingAnimation
import com.theapache64.stackzy.util.R


/**
 * To select an application from the selected device
 */
@Composable
fun SelectAppScreen(
    appListViewModel: AppListViewModel,
    onBackClicked: () -> Unit,
    onAppSelected: (AndroidAppWrapper) -> Unit
) {

    val searchKeyword by appListViewModel.searchKeyword.collectAsState()
    val appsResponse by appListViewModel.apps.collectAsState()
    val selectedTabIndex by appListViewModel.selectedTabIndex.collectAsState()

    val hasData = appsResponse is Resource.Success
            && (appsResponse as Resource.Success<List<AndroidAppWrapper>>).data.isNotEmpty()

    CustomScaffold(
        title = R.string.select_app_title,
        onBackClicked = onBackClicked,
        bottomGradient = hasData, // only for success
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
                    appListViewModel.onSearchKeywordChanged(it)
                },
                modifier = Modifier
                    .width(300.dp)
            )
        }
    ) {

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Column {

            if (selectedTabIndex != AppListViewModel.TAB_NO_TAB) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(
                        top = 5.dp,
                        bottom = 10.dp,
                    )
                ) {
                    AppListViewModel.tabsMap.entries.forEach { tabEntry ->
                        Tab(
                            selected = tabEntry.key == selectedTabIndex,
                            onClick = { appListViewModel.onTabClicked(tabEntry.key) },
                            text = { Text(tabEntry.value) }
                        )
                    }
                }
            }

            when (appsResponse) {

                is Resource.Loading -> {
                    val message = (appsResponse as Resource.Loading<List<AndroidAppWrapper>>).message ?: ""
                    LoadingAnimation(message, funFacts = null)
                }
                is Resource.Error -> {
                    Box {
                        ErrorSnackBar(
                            (appsResponse as Resource.Error<List<AndroidAppWrapper>>).errorData
                        )
                    }
                }
                is Resource.Success -> {
                    val apps = (appsResponse as Resource.Success<List<AndroidAppWrapper>>).data

                    if (apps.isNotEmpty()) {
                        // Grid
                        LazyVerticalGrid(
                            cells = GridCells.Fixed(3),
                        ) {
                            items(items = apps) { app ->
                                Column {
                                    // GridItem
                                    Selectable(
                                        data = app,
                                        onSelected = onAppSelected
                                    )

                                    Spacer(
                                        modifier = Modifier.height(10.dp)
                                    )
                                }
                            }
                        }

                    } else {
                        // No app found
                        FullScreenError(
                            title = "App not found",
                            message = "Couldn't find any app with $searchKeyword",
                            image = painterResource("drawables/woman_desk.png"),
                            action = {
                                Button(
                                    onClick = {
                                        appListViewModel.onOpenMarketClicked()
                                    },
                                ) {
                                    Text(text = R.string.app_detail_action_open_market)
                                }
                            }
                        )
                    }
                }
                null -> {
                    LoadingAnimation("Preparing apps...", funFacts = null)
                }
            }
        }

    }
}