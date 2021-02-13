package com.theapache64.stackzy.ui.feature.appdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.ui.common.ContentScreen
import com.theapache64.stackzy.ui.common.LoadingText
import com.theapache64.stackzy.util.R

@Composable
fun AppDetailScreen(
    appDetailViewModel: AppDetailViewModel,
    onLibrarySelected: (Library) -> Unit,
    onBackClicked: () -> Unit
) {
    val loadingMessage by appDetailViewModel.loadingMessage.collectAsState()

    ContentScreen(
        title = R.string.app_detail_title,
        onBackClicked = onBackClicked
    ) {
        if (loadingMessage != null) {
            LoadingText(
                message = loadingMessage!!
            )
        }
    }
}