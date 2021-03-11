package com.theapache64.stackzy.ui.feature.selectapp

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.ui.navigation.Component
import com.theapache64.stackzy.util.Either
import javax.inject.Inject

class SelectAppScreenComponent(
    componentContext: ComponentContext,
    appComponent: AppComponent,
    private val source: Either<AndroidDevice, Account>,
    val onAppSelected: (Either<AndroidDevice, Account>, AndroidApp) -> Unit,
    val onBackClicked: () -> Unit
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var selectAppViewModel: SelectAppViewModel

    init {
        appComponent.inject(this)
        selectAppViewModel.init(source)
    }

    @Composable
    override fun render() {
        SelectAppScreen(
            selectAppViewModel = selectAppViewModel,
            onBackClicked = onBackClicked,
            onAppSelected = {
                onAppSelected(source, it)
            }
        )
    }
}
