package com.theapache64.stackzy.ui.feature.libdetail

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.theapache64.stackzy.model.AndroidAppWrapper

@Composable
fun LibraryDetailScreen(
    viewModel: LibraryDetailViewModel,
    onBackClicked: () -> Unit,
    onAppClicked: (AndroidAppWrapper) -> Unit,
) {
    Text("Something")
}