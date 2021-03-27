package com.theapache64.stackzy.ui.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.di.DaggerAppComponent
import com.theapache64.stackzy.ui.feature.appdetail.AppDetailScreenComponent
import com.theapache64.stackzy.ui.feature.login.LogInScreenComponent
import com.theapache64.stackzy.ui.feature.pathway.PathwayScreenComponent
import com.theapache64.stackzy.ui.feature.selectapp.SelectAppScreenComponent
import com.theapache64.stackzy.ui.feature.selectdevice.SelectDeviceScreenComponent
import com.theapache64.stackzy.ui.feature.splash.SplashScreenComponent
import com.theapache64.stackzy.util.ApkSource
import com.toxicbakery.logging.Arbor
import java.awt.Desktop
import java.net.URI

/**
 * All navigation decisions are made from here
 */
class NavHostComponent(
    private val componentContext: ComponentContext
) : Component, ComponentContext by componentContext {

    /**
     * Available screens
     */
    private sealed class Config : Parcelable {
        object Splash : Config()
        object SelectPathway : Config()
        object LogIn : Config()
        object SelectDevice : Config()
        data class SelectApp(
            val apkSource: ApkSource<AndroidDevice, Account>
        ) : Config()

        data class AppDetail(
            val apkSource: ApkSource<AndroidDevice, Account>,
            val androidApp: AndroidApp
        ) : Config()
    }

    private val appComponent: AppComponent = DaggerAppComponent
        .create()

    /**
     * Router configuration
     */
    private val router = router<Config, Component>(
        initialConfiguration = Config.Splash,
        componentFactory = ::createScreenComponent
    )

    /**
     * When a new navigation request made, the screen will be created by this method.
     */
    private fun createScreenComponent(config: Config, componentContext: ComponentContext): Component {
        return when (config) {
            is Config.Splash -> SplashScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                onSyncFinished = ::onSplashSyncFinished
            )
            is Config.SelectPathway -> PathwayScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                onAdbSelected = ::onPathwayAdbSelected,
                onLogInNeeded = ::onLogInNeeded,
                onPlayStoreSelected = ::onPathwayPlayStoreSelected
            )
            is Config.LogIn -> LogInScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                onLoggedIn = ::onLoggedIn,
                onBackClicked = ::onBackClicked
            )

            is Config.SelectDevice -> SelectDeviceScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                onDeviceSelected = ::onDeviceSelected,
                onBackClicked = ::onBackClicked,
            )
            is Config.SelectApp -> SelectAppScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                apkSource = config.apkSource,
                onAppSelected = ::onAppSelected,
                onBackClicked = ::onBackClicked
            )

            is Config.AppDetail -> AppDetailScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                selectedApp = config.androidApp,
                apkSource = config.apkSource,
                onLibrarySelected = ::onLibrarySelected,
                onBackClicked = ::onBackClicked
            )
        }
    }

    @Composable
    override fun render() {
        Children(routerState = router.state) { child, _ ->
            child.render()
        }
    }

    /**
     * Invoked when splash finish data sync
     */
    private fun onSplashSyncFinished() {
        router.push(Config.SelectPathway)
        /*router.push(
            Config.AppDetail(
                AndroidDevice(
                    "Samsung",
                    "someModel",
                    Device(
                        "R52M604X18E",
                        DeviceState.DEVICE
                    )
                ),
                AndroidApp(
                    // Package("a.i"),
                    Package("com.theapache64.topcorn"),
                )
            )
        )*/
    }

    /**
     * Invoked when play store selected from the pathway screen
     */
    private fun onPathwayPlayStoreSelected(account: Account) {
        Arbor.d("Showing select app")
        router.push(Config.SelectApp(ApkSource.PlayStore(account)))
    }

    private fun onLogInNeeded() {
        router.push(Config.LogIn)
    }

    private fun onLoggedIn(account: Account) {
        router.pop() // remove login screen from stack

        // then go to select app screen
        router.push(Config.SelectApp(ApkSource.PlayStore(account)))
    }

    /**
     * Invoked when adb selected from the pathway screen
     */
    private fun onPathwayAdbSelected() {
        router.push(Config.SelectDevice)
    }

    /**
     * Invoked when a device selected
     */
    private fun onDeviceSelected(androidDevice: AndroidDevice) {
        router.push(Config.SelectApp(ApkSource.Adb(androidDevice)))
    }

    /**
     * Invoked when the app got selected
     */
    private fun onAppSelected(
        apkSource: ApkSource<AndroidDevice, Account>,
        androidApp: AndroidApp
    ) {
        router.push(
            Config.AppDetail(
                apkSource = apkSource,
                androidApp = androidApp
            )
        )
    }

    /**
     * Invoked when library selected
     */
    private fun onLibrarySelected(library: Library) {
        Desktop.getDesktop().browse(URI(library.website))
    }

    /**
     * Invoked when back arrow pressed
     */
    private fun onBackClicked() {
        Arbor.d("Back clicked popping")
        router.pop()
    }


}