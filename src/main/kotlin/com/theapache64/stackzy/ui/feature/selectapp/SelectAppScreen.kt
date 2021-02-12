package com.theapache64.stackzy.ui.feature.selectapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.theapache64.stackzy.ui.common.ContentScreen
import com.theapache64.stackzy.util.R

@Composable
fun SelectAppScreen(
    selectAppViewModel: SelectAppViewModel,
    onBackClicked: () -> Unit
) {

    val apps by selectAppViewModel.apps.collectAsState()

    ContentScreen(
        title = R.string.select_app_title,
        onBackClicked = onBackClicked
    ) {

    }
}