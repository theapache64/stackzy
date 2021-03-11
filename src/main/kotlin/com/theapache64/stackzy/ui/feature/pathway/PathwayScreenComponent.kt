package com.theapache64.stackzy.ui.feature.pathway

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.ui.navigation.Component
import javax.inject.Inject

class PathwayScreenComponent(
    appComponent: AppComponent,
    private val componentContext: ComponentContext,
    private val onAdbSelected: () -> Unit,
    private val onPlayStoreSelected: (Account) -> Unit,
    private val onLogInNeeded: () -> Unit,
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var viewModel: PathwayViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    override fun render() {
        PathwayScreen(
            viewModel = viewModel,
            onAdbSelected = onAdbSelected,
            onPlayStoreSelected = onPlayStoreSelected,
            onLogInNeeded = onLogInNeeded
        )
    }

}