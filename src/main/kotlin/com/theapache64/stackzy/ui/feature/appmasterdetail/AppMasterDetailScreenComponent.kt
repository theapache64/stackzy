package com.theapache64.stackzy.ui.feature.appmasterdetail

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.model.AndroidDeviceWrapper
import com.theapache64.stackzy.model.LibraryWrapper
import com.theapache64.stackzy.ui.feature.appdetail.AppDetailViewModel
import com.theapache64.stackzy.ui.feature.applist.AppListViewModel
import com.theapache64.stackzy.ui.navigation.Component
import com.theapache64.stackzy.util.ApkSource
import javax.inject.Inject

class AppMasterDetailScreenComponent(
    componentContext: ComponentContext,
    appComponent: AppComponent,
    private val apkSource: ApkSource<AndroidDeviceWrapper, Account>,
    private val onBackClicked: () -> Unit,
    private val onLibrarySelected: (libraryWrapper: LibraryWrapper) -> Unit,
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var appMasterDetailViewModel: AppMasterDetailViewModel

    @Inject
    lateinit var appListViewModel: AppListViewModel

    @Inject
    lateinit var appDetailViewModel: AppDetailViewModel

    init {

        appComponent.inject(this)
    }

    @Composable
    override fun render() {
        AppMasterDetailsScreen(
            appListViewModel,
            onAppSelected = {
                appDetailViewModel.onAppSelected(it)
            },
            apkSource,
            appDetailViewModel,
            onLibrarySelected = onLibrarySelected,
            onBackClicked = onBackClicked
        )
    }
}
