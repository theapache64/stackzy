package com.theapache64.stackzy.ui.feature.selectapp

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.ui.common.CONTENT_PADDING_HORIZONTAL
import com.theapache64.stackzy.ui.common.CustomScaffold
import com.theapache64.stackzy.ui.common.Selectable
import com.theapache64.stackzy.util.R

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
    val apps by selectAppViewModel.apps.collectAsState()

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
    }
}