package com.theapache64.stackzy.ui.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.ui.navigation.Component
import javax.inject.Inject

class LogInScreenComponent(
    appComponent: AppComponent,
    private val componentContext: ComponentContext,
    private val onLoggedIn: (shouldGoToPlayStore: Boolean, Account) -> Unit,
    private val onBackClicked: () -> Unit,
    private val shouldGoToPlayStore: Boolean
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var viewModel: LogInScreenViewModel

    init {
        appComponent.inject(this)
    }

    @Composable
    override fun render() {

        val scope = rememberCoroutineScope()
        LaunchedEffect(viewModel) {
            viewModel.init(scope, onLoggedIn, shouldGoToPlayStore)
        }

        LogInScreen(
            viewModel = viewModel,
            onBackClicked = onBackClicked
        )
    }

}