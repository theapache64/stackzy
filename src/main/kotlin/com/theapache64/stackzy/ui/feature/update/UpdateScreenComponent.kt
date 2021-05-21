package com.theapache64.stackzy.ui.feature.update

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.ui.navigation.Component
import javax.inject.Inject

class UpdateScreenComponent(
    appComponent: AppComponent,
    private val componentContext: ComponentContext
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var viewModel: UpdateScreenViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    override fun render() {
        UpdateScreen(
            viewModel
        )
    }
}