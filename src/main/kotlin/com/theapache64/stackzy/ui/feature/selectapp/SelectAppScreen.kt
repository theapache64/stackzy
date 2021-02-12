package com.theapache64.stackzy.ui.feature.selectapp

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.theapache64.stackzy.ui.common.ContentScreen
import com.theapache64.stackzy.util.R

@Composable
fun SelectAppScreen(
    selectAppViewModel: SelectAppViewModel,
    onBackClicked: () -> Unit
) {
    ContentScreen(
        title = R.string.select_app_title,
        onBackClicked = onBackClicked
    ) {
        Text(text = selectAppViewModel.selectedDevice.name)
    }
}