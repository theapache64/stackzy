package com.theapache64.stackzy.ui.feature.libdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.model.LibraryWrapper
import com.theapache64.stackzy.ui.navigation.Component
import javax.inject.Inject

class LibraryDetailScreenComponent(
    componentContext: ComponentContext,
    appComponent: AppComponent,
    val libraryWrapper: LibraryWrapper,
    val onBackClicked: () -> Unit
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
            
            libDetailViewModel.init(scope, libraryWrapper)
            libDetailViewModel.loadApps()
        }

        LibraryDetailScreen(
            viewModel = libDetailViewModel,
            onAppClicked = {

            },
            onBackClicked = onBackClicked
        )
    }
}