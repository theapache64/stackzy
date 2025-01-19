package com.theapache64.stackzy.ui.feature.libdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.model.AndroidAppWrapper
import com.theapache64.stackzy.model.AndroidDeviceWrapper
import com.theapache64.stackzy.model.LibraryWrapper
import com.theapache64.stackzy.ui.navigation.Component
import com.theapache64.stackzy.util.ApkSource
import javax.inject.Inject

class LibraryDetailScreenComponent(
    componentContext: ComponentContext,
    appComponent: AppComponent,
    val libraryWrapper: LibraryWrapper,
    val onAppClicked: (ApkSource, AndroidAppWrapper) -> Unit,
    val onBackClicked: () -> Unit,
    val onLogInNeeded: (Boolean) -> Unit
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var libDetailViewModel: LibraryDetailViewModel

    init {

        appComponent.inject(this)
    }

    @Composable
    override fun render() {
        val scope = rememberCoroutineScope()
        LaunchedEffect(libDetailViewModel) {

            libDetailViewModel.init(scope, libraryWrapper, onAppClicked, onLogInNeeded)

            if (libDetailViewModel.apps.value == null) {
                libDetailViewModel.loadApps()
            }
        }

        LibraryDetailScreen(
            viewModel = libDetailViewModel,
            onBackClicked = onBackClicked
        )
    }
}