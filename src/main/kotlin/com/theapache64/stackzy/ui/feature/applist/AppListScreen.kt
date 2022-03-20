package com.theapache64.stackzy.ui.feature.applist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.theapache64.stackzy.model.AndroidAppWrapper
import com.theapache64.stackzy.model.AndroidDeviceWrapper
import com.theapache64.stackzy.ui.common.ErrorSnackBar
import com.theapache64.stackzy.ui.common.FullScreenError
import com.theapache64.stackzy.ui.common.Selectable
import com.theapache64.stackzy.ui.common.loading.LoadingAnimation
import com.theapache64.stackzy.util.ApkSource
import com.theapache64.stackzy.util.R


/**
 * To select an application from the selected device
 */
@Composable
fun SelectAppScreen(
    appListViewModel: AppListViewModel,
    apkSource: ApkSource<AndroidDeviceWrapper, Account>,
    onAppSelected: (AndroidAppWrapper) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(appListViewModel, apkSource) {
        appListViewModel.init(scope, apkSource)
    }

    val searchKeyword by appListViewModel.searchKeyword.collectAsState()
    val appsResponse by appListViewModel.apps.collectAsState()

    Column(modifier = modifier) {

        // SearchBox
        TextField(
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
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(10.dp))

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
                    var prevSelectedApp = remember<AndroidAppWrapper?> { null }
                    // Grid
                    LazyColumn {
                        items(items = apps, key = { it.appPackage.name }) { app ->
                            Column {
                                // GridItem
                                Selectable(
                                    data = app,
                                    onSelected = { app ->
                                        prevSelectedApp?.isAppActive = false
                                        onAppSelected(app)
                                        app.isAppActive = true
                                        prevSelectedApp = app
                                    }
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