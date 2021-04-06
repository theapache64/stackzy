package com.theapache64.stackzy.ui.feature.appdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.di.AppComponent
import com.theapache64.stackzy.model.AndroidAppWrapper
import com.theapache64.stackzy.model.AndroidDeviceWrapper
import com.theapache64.stackzy.model.LibraryWrapper
import com.theapache64.stackzy.ui.navigation.Component
import com.theapache64.stackzy.util.ApkSource
import javax.inject.Inject

class AppDetailScreenComponent(
    appComponent: AppComponent,
    componentContext: ComponentContext,
    private val selectedApp: AndroidAppWrapper,
    private val apkSource: ApkSource<AndroidDeviceWrapper, Account>,
    val onLibrarySelected: (LibraryWrapper) -> Unit,
    private val onBackClicked: () -> Unit
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var appDetailViewModel: AppDetailViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    override fun render() {

        val scope = rememberCoroutineScope()
        LaunchedEffect(appDetailViewModel) {
            appDetailViewModel.init(
                scope = scope,
                apkSource = apkSource,
                androidApp = selectedApp,
            )
            appDetailViewModel.startDecompile()
        }

        AppDetailScreen(
            viewModel = appDetailViewModel,
            onLibrarySelected = onLibrarySelected,
            onBackClicked = onBackClicked
        )
    }
}
