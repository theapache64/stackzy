package com.theapache64.stackzy.ui.feature.appmasterdetail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.model.AndroidAppWrapper
import com.theapache64.stackzy.model.AndroidDeviceWrapper
import com.theapache64.stackzy.model.LibraryWrapper
import com.theapache64.stackzy.ui.common.CustomScaffold
import com.theapache64.stackzy.ui.feature.appdetail.AppDetailScreen
import com.theapache64.stackzy.ui.feature.appdetail.AppDetailViewModel
import com.theapache64.stackzy.ui.feature.applist.AppListViewModel
import com.theapache64.stackzy.ui.feature.applist.SelectAppScreen
import com.theapache64.stackzy.util.ApkSource


/**
 * To select an application from the selected device
 */
@Composable
fun AppMasterDetailsScreen(
    appListViewModel: AppListViewModel,
    onAppSelected: (AndroidAppWrapper) -> Unit,
    apkSource: ApkSource<AndroidDeviceWrapper, Account>,

    appDetailViewModel: AppDetailViewModel,
    onLibrarySelected: (LibraryWrapper) -> Unit,

    onBackClicked: () -> Unit
) {

    CustomScaffold(
        title = "Master Detail Demo",
        onBackClicked = onBackClicked,
        bottomGradient = false
    ) {
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        Row {
            SelectAppScreen(
                modifier = Modifier.weight(weight = 0.2f),
                appListViewModel = appListViewModel,
                apkSource = apkSource,
                onAppSelected = onAppSelected
            )
            Spacer(modifier = Modifier.width(20.dp))
            AppDetailScreen(
                modifier = Modifier.weight(weight = 0.8f),
                viewModel = appDetailViewModel,
                onLibrarySelected = onLibrarySelected,
                apkSource = apkSource
            )
        }
    }
}
