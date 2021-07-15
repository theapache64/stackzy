package com.theapache64.stackzy.ui.feature.libdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.ui.navigation.Component
import javax.inject.Inject

class LibraryDetailScreenComponent(
    componentContext: ComponentContext,
    appComponent: AppComponent,
    val onBackClicked: () -> Unit
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var libraryDetailViewModel: LibraryDetailViewModel

    init {
        println("Create new select app screen component")
        appComponent.inject(this)
    }

    @Composable
    override fun render() {
        val scope = rememberCoroutineScope()
        LaunchedEffect(libraryDetailViewModel) {
            println("Creating select app screen...")
            libraryDetailViewModel.init(scope)
        }

        LibraryDetailScreen(
            viewModel = libraryDetailViewModel,
            onAppClicked = {

            },
            onBackClicked = onBackClicked
        )
    }
}