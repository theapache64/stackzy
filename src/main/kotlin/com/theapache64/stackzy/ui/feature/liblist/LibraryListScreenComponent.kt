package com.theapache64.stackzy.ui.feature.liblist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.model.LibraryWrapper
import com.theapache64.stackzy.ui.navigation.Component
import com.toxicbakery.logging.Arbor
import javax.inject.Inject

class LibraryListScreenComponent(
    componentContext: ComponentContext,
    appComponent: AppComponent,
    val onLibraryClicked: (LibraryWrapper) -> Unit,
    val onBackClicked: () -> Unit
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var libraryListViewModel: LibraryListViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    override fun render() {
        val scope = rememberCoroutineScope()
        LaunchedEffect(libraryListViewModel) {
            libraryListViewModel.init(scope)

            if (libraryListViewModel.libsResp.value == null) {
                libraryListViewModel.loadLibraries()
                Arbor.d("render: Loading libraries.. ")
            }
        }

        LibraryListScreen(
            viewModel = libraryListViewModel,
            onLibraryClicked = onLibraryClicked,
            onBackClicked = onBackClicked
        )
    }
}