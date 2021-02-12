package com.theapache64.stackzy.ui.feature.selectapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
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

@Composable
fun SelectAppScreen(
    selectAppViewModel: SelectAppViewModel,
    onBackClicked: () -> Unit,
    onAppSelected: (AndroidApp) -> Unit
) {

    val apps by selectAppViewModel.apps.collectAsState()

    ContentScreen(
        title = R.string.select_app_title,
        onBackClicked = onBackClicked
    ) {
        LazyColumn {
            items(apps) { app ->
                Selectable(
                    data = app,
                    icon = {
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
                    },
                    onSelected = onAppSelected
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }
        }
    }
}