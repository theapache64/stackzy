package com.theapache64.stackzy.ui.feature.appdetail

import androidx.compose.runtime.Composable
import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.ui.common.ContentScreen
import com.theapache64.stackzy.util.R

@Composable
fun AppDetailScreen(
    appDetailViewModel: AppDetailViewModel,
    onLibrarySelected: (Library) -> Unit,
    onBackClicked: () -> Unit
) {
    ContentScreen(
        title = R.string.app_detail_title,
        onBackClicked = onBackClicked
    ) {

    }
}