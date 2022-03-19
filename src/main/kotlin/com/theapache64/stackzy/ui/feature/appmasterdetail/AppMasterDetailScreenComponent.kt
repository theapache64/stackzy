package com.theapache64.stackzy.ui.feature.appmasterdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.model.AndroidAppWrapper
import com.theapache64.stackzy.model.AndroidDeviceWrapper
import com.theapache64.stackzy.ui.navigation.Component
import com.theapache64.stackzy.util.ApkSource
import javax.inject.Inject

class AppMasterDetailScreenComponent(
    componentContext: ComponentContext,
    appComponent: AppComponent,
    private val apkSource: ApkSource<AndroidDeviceWrapper, Account>,
    val onAppSelected: (ApkSource<AndroidDeviceWrapper, Account>, AndroidAppWrapper) -> Unit,
    val onBackClicked: () -> Unit
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var appListViewModel: AppMasterDetailViewModel

    init {

        appComponent.inject(this)
    }

    @Composable
    override fun render() {
        val scope = rememberCoroutineScope()
        LaunchedEffect(appListViewModel) {

            appListViewModel.init(scope, apkSource)
            if (appListViewModel.apps.value == null) {
                appListViewModel.loadApps()
            }
        }

        SelectAppScreen(
            appListViewModel = appListViewModel,
            onBackClicked = onBackClicked,
            onAppSelected = { app ->
                onAppSelected(apkSource, app)
            }
        )
    }
}
