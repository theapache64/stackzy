package com.theapache64.stackzy.ui.feature.selectapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.ui.common.ContentScreen
import com.theapache64.stackzy.ui.common.Selectable
import com.theapache64.stackzy.util.R

@Composable
fun SelectAppScreen(
    selectAppViewModel: SelectAppViewModel,
    onBackClicked: () -> Unit,
    onAppSelected: (AndroidApp) -> Unit
) {

    var searchKeyword by remember { mutableStateOf("") }
    val apps by selectAppViewModel.apps.collectAsState()

    ContentScreen(
        title = R.string.select_app_title,
        onBackClicked = onBackClicked,
        topRightSlot = {
            OutlinedTextField(
                value = searchKeyword,
                onValueChange = {
                    searchKeyword = it
                    selectAppViewModel.onSearchKeywordChanged(it)
                },
                modifier = Modifier
                    .padding(start = 200.dp)
                    .fillMaxWidth()
            )
        }
    ) {
        LazyColumn {
            items(apps.chunked(2)) { app ->
                Row {
                    Selectable(
                        data = app[0],
                        icon = {
                            AppIcon()
                        },
                        onSelected = onAppSelected
                    )

                    Selectable(
                        data = app[1],
                        icon = {
                            AppIcon()
                        },
                        onSelected = onAppSelected
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }
        }
    }
}

private fun AppIcon() {
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