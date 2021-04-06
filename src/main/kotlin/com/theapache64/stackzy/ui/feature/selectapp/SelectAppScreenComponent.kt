package com.theapache64.stackzy.ui.feature.selectapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.ui.navigation.Component
import com.theapache64.stackzy.util.ApkSource
import javax.inject.Inject

class SelectAppScreenComponent(
    componentContext: ComponentContext,
    appComponent: AppComponent,
    private val apkSource: ApkSource<AndroidDevice, Account>,
    val onAppSelected: (ApkSource<AndroidDevice, Account>, AndroidApp) -> Unit,
    val onBackClicked: () -> Unit
) : Component, ComponentContext by componentContext {

    @Inject
    lateinit var selectAppViewModel: SelectAppViewModel

    init {
        println("Create new select app screen component")
        appComponent.inject(this)
    }

    @Composable
    override fun render() {
        val scope = rememberCoroutineScope()
        LaunchedEffect(selectAppViewModel) {
            println("Creating select app screen...")
            selectAppViewModel.init(scope, apkSource)
            if (selectAppViewModel.apps.value == null) {
                selectAppViewModel.loadApps()
            }
        }

        SelectAppScreen(
            selectAppViewModel = selectAppViewModel,
            onBackClicked = onBackClicked,
            onAppSelected = {
                onAppSelected(apkSource, it)
            }
        )
    }
}
