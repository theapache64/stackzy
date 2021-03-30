package com.theapache64.stackzy.ui.feature.appdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.arkivanov.decompose.ComponentContext
import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.ui.navigation.Component
import com.theapache64.stackzy.util.ApkSource
import javax.inject.Inject

class AppDetailScreenComponent(
    appComponent: AppComponent,
    componentContext: ComponentContext,
    private val selectedApp: AndroidApp,
    private val apkSource: ApkSource<AndroidDevice, Account>,
    val onLibrarySelected: (Library) -> Unit,
    private val onBackClicked: () -> Unit
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var appDetailViewModel: AppDetailViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    override fun render() {

        LaunchedEffect(appDetailViewModel) {
            appDetailViewModel.init(
                scope = this,
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
