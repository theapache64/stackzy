package com.theapache64.stackzy.ui.feature.liblist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.ui.navigation.Component
import javax.inject.Inject

class LibraryListScreenComponent(
    componentContext: ComponentContext,
    appComponent: AppComponent,
    val onBackClicked: () -> Unit
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var libraryListViewModel: LibraryListViewModel

    init {
        println("Create new select app screen component")
        appComponent.inject(this)
    }

    @Composable
    override fun render() {
        val scope = rememberCoroutineScope()
        LaunchedEffect(libraryListViewModel) {
            println("Creating select app screen...")
            libraryListViewModel.init(scope)
        }

        LibraryListScreen(
            viewModel = libraryListViewModel,
            onLibraryClicked = {

            },
            onBackClicked = onBackClicked
        )
    }
}