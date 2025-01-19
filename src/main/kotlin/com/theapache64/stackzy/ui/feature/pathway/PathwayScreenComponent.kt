package com.theapache64.stackzy.ui.feature.pathway

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.arkivanov.decompose.ComponentContext
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.ui.navigation.Component
import javax.inject.Inject

class PathwayScreenComponent(
    appComponent: AppComponent,
    private val componentContext: ComponentContext,
    private val onAdbSelected: () -> Unit,
    private val onLibrariesSelected: () -> Unit,
    onPlayStoreSelected: () -> Unit,
    onLogInNeeded: (shouldGoToPlayStore: Boolean) -> Unit,
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var viewModel: PathwayViewModel

    init {
        appComponent.inject(this)

        viewModel.init(
            onPlayStoreSelected = onPlayStoreSelected,
            onLogInNeeded = {
                onLogInNeeded(true)
            }
        )
    }

    @Composable
    override fun render() {
        LaunchedEffect(viewModel) {
            viewModel.refreshAccount()
        }

        PathwayScreen(
            viewModel = viewModel,
            onAdbSelected = onAdbSelected,
            onLibrariesSelected = onLibrariesSelected
        )
    }

}