package com.theapache64.stackzy.ui.feature.selectapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.ComponentContext
import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.di.AppComponent
import com.theapache64.stackzy.model.AndroidAppWrapper
import com.theapache64.stackzy.model.AndroidDeviceWrapper
import com.theapache64.stackzy.ui.navigation.Component
import com.theapache64.stackzy.util.ApkSource
import javax.inject.Inject

class SelectAppScreenComponent(
    componentContext: ComponentContext,
    appComponent: AppComponent,
    private val apkSource: ApkSource<AndroidDeviceWrapper, Account>,
    val onAppSelected: (ApkSource<AndroidDeviceWrapper, Account>, AndroidAppWrapper) -> Unit,
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
