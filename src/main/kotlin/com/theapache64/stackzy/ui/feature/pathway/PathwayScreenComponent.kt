package com.theapache64.stackzy.ui.feature.pathway

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.arkivanov.decompose.ComponentContext
import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.di.AppComponent
import com.theapache64.stackzy.ui.navigation.Component
import javax.inject.Inject

class PathwayScreenComponent(
    private val appComponent: AppComponent,
    private val componentContext: ComponentContext,
    private val onAdbSelected: () -> Unit,
    onPlayStoreSelected: (Account) -> Unit,
    onLogInNeeded: () -> Unit,
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var viewModel: PathwayViewModel

    init {
        appComponent.inject(this)

        viewModel.init(
            onPlayStoreSelected = onPlayStoreSelected,
            onLogInNeeded = onLogInNeeded
        )
    }

    @Composable
    override fun render() {
        LaunchedEffect(viewModel) {
            viewModel.refreshAccount()
        }

        PathwayScreen(
            viewModel = viewModel,
            onAdbSelected = onAdbSelected
        )
    }

}