package com.theapache64.stackzy.ui.feature.selectapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.ui.common.ContentScreen
import com.theapache64.stackzy.ui.common.Selectable
import com.theapache64.stackzy.util.R

private const val GRID_SIZE = 2

@Composable
fun SelectAppScreen(
    selectAppViewModel: SelectAppViewModel,
    onBackClicked: () -> Unit,
    onAppSelected: (AndroidApp) -> Unit
) {

    val searchKeyword by selectAppViewModel.searchKeyword.collectAsState()
    val apps by selectAppViewModel.apps.collectAsState()

    ContentScreen(
        title = R.string.select_app_title,
        onBackClicked = onBackClicked,
        topRightSlot = {

            // SearchBox
            OutlinedTextField(
                value = searchKeyword,
                label = {
                    Text(text = R.string.select_app_label_search)
                },
                onValueChange = {
                    selectAppViewModel.onSearchKeywordChanged(it)
                },
                modifier = Modifier
                    .padding(start = 200.dp)
                    .fillMaxWidth()
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
                            modifier = Modifier.width(500.dp),
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

@Composable
fun AppIcon() {
    Image(
        modifier = Modifier
            .padding(
                horizontal = 20.dp,
                vertical = 10.dp
            )
            .size(30.dp),
        bitmap = imageResource("drawables/app.png"),
        contentDescription = R.string.apps_cd_app_icon,
        colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
    )
}