package com.theapache64.stackzy.ui.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfadeScale
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.replaceCurrent
import com.arkivanov.decompose.router.router
import com.arkivanov.essenty.parcelable.Parcelable
import com.github.theapache64.gpa.model.Account
import com.malinskiy.adam.request.device.Device
import com.malinskiy.adam.request.device.DeviceState
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.di.AppComponent
import com.theapache64.stackzy.di.DaggerAppComponent
import com.theapache64.stackzy.model.AndroidAppWrapper
import com.theapache64.stackzy.model.AndroidDeviceWrapper
import com.theapache64.stackzy.model.LibraryWrapper
import com.theapache64.stackzy.ui.feature.appdetail.AppDetailScreenComponent
import com.theapache64.stackzy.ui.feature.applist.AppListScreenComponent
import com.theapache64.stackzy.ui.feature.appmasterdetail.AppMasterDetailScreenComponent
import com.theapache64.stackzy.ui.feature.devicelist.DeviceListScreenComponent
import com.theapache64.stackzy.ui.feature.libdetail.LibraryDetailScreenComponent
import com.theapache64.stackzy.ui.feature.liblist.LibraryListScreenComponent
import com.theapache64.stackzy.ui.feature.login.LogInScreenComponent
import com.theapache64.stackzy.ui.feature.pathway.PathwayScreenComponent
import com.theapache64.stackzy.ui.feature.splash.SplashScreenComponent
import com.theapache64.stackzy.ui.feature.update.UpdateScreenComponent
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
     * Available screensSelectApp
     */
    private sealed class Config : Parcelable {
        object Splash : Config()
        object SelectPathway : Config()
        data class LogIn(val shouldGoToPlayStore: Boolean) : Config()
        object DeviceList : Config()
        data class AppMasterDetail(
            val apkSource: ApkSource<AndroidDeviceWrapper, Account>
        ) : Config()

        data class AppList(
            val apkSource: ApkSource<AndroidDeviceWrapper, Account>
        ) : Config()

        data class AppDetail(
            val apkSource: ApkSource<AndroidDeviceWrapper, Account>,
            val androidApp: AndroidAppWrapper
        ) : Config()

        object Update : Config()
        object LibraryList : Config()
        data class LibraryDetail(
            val libraryWrapper: LibraryWrapper
        ) : Config()
    }

    private val appComponent: AppComponent = DaggerAppComponent
        .create()

    /**
     * Router configuration
     */
    private val router = router<Config, Component>(
        initialConfiguration = Config.Splash,
        childFactory = ::createScreenComponent
    )

    /**
     * When a new navigation request made, the screen will be created by this method.
     */
    private fun createScreenComponent(config: Config, componentContext: ComponentContext): Component {
        return when (config) {
            is Config.Splash -> SplashScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                onSyncFinished = ::onSplashSyncFinished,
                onUpdateNeeded = ::onUpdateNeeded
            )
            is Config.SelectPathway -> PathwayScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                onAdbSelected = ::onPathwayAdbSelected,
                onLogInNeeded = ::onLogInNeeded,
                onPlayStoreSelected = ::onPathwayPlayStoreSelected,
                onLibrariesSelected = ::onPathwayLibrariesSelected
            )
            is Config.LogIn -> LogInScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                onLoggedIn = ::onLoggedIn,
                onBackClicked = ::onBackClicked,
                shouldGoToPlayStore = config.shouldGoToPlayStore
            )

            is Config.DeviceList -> DeviceListScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                onDeviceSelected = ::onDeviceSelected,
                onBackClicked = ::onBackClicked,
            )
            is Config.AppList -> AppListScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                apkSource = config.apkSource,
                onAppSelected = ::onAppSelected,
                onBackClicked = ::onBackClicked
            )

            is Config.AppMasterDetail -> AppMasterDetailScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                apkSource = config.apkSource,
                onBackClicked = ::onBackClicked,
                onLibrarySelected = ::onLibrarySelected
            )

            is Config.AppDetail -> AppDetailScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                selectedApp = config.androidApp,
                apkSource = config.apkSource,
                onLibrarySelected = ::onLibrarySelected,
                onBackClicked = ::onBackClicked
            )

            is Config.Update -> UpdateScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext
            )
            is Config.LibraryList -> LibraryListScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                onLibraryClicked = ::onLibraryClicked,
                onBackClicked = ::onBackClicked
            )

            is Config.LibraryDetail -> LibraryDetailScreenComponent(
                appComponent = appComponent,
                componentContext = componentContext,
                onBackClicked = ::onBackClicked,
                onAppClicked = ::onAppSelected,
                libraryWrapper = config.libraryWrapper,
                onLogInNeeded = ::onLogInNeeded
            )
        }
    }

    /**
     * Invoked when a library clicked
     */
    private fun onLibraryClicked(libraryWrapper: LibraryWrapper) {
        router.push(Config.LibraryDetail(libraryWrapper))
    }


    @OptIn(ExperimentalDecomposeApi::class)
    @Composable
    override fun render() {
        Children(
            routerState = router.state,
            animation = crossfadeScale()
        ) { child ->
            child.instance.render()
        }
    }

    /**
     * Invoked when splash finish data sync
     */
    private fun onSplashSyncFinished() {
        // router.replaceCurrent(Config.SelectPathway)
        router.replaceCurrent(
            Config.AppMasterDetail(
                apkSource = if(false){
                    ApkSource.Adb(
                        value = AndroidDeviceWrapper(
                            androidDevice = AndroidDevice(
                                name = "OnePlus",
                                model = "A6000",
                                device = Device(
                                    serial = "192.168.1.5:5555",
                                    state = DeviceState.DEVICE
                                )
                            )
                        )
                    )
                }else{
                    ApkSource.PlayStore(
                        Account(
                            username = "mymailer64",
                            password = "YOURPASSWORDGOESHERE",
                            token = "GgiNgFkXmZDpLanov77qhqHwrzA3_BfmD7Cfgy3I-PZlkw9ihJ_B15_4U1VrEd0skxDZCg.",
                            gsfId = "3992ef54bcc0d9de",
                            locale = "en-IN"
                        )
                    )
                }
            ),
        )
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
        router.push(Config.AppList(ApkSource.PlayStore(account)))
    }

    /**
     * This method will be invoked when login is needed (either login pressed or authentication failed)
     */
    private fun onLogInNeeded(shouldGoToPlayStore: Boolean) {
        router.push(Config.LogIn(shouldGoToPlayStore))
    }

    /**
     * Invoked when login succeeded.
     */
    private fun onLoggedIn(shouldGoToPlayStore: Boolean, account: Account) {
        if (shouldGoToPlayStore) {
            router.replaceCurrent(Config.AppList(ApkSource.PlayStore(account)))
        } else {
            Arbor.d("onLoggedIn: Moving back...")
            router.pop()
        }
    }

    /**
     * Invoked when adb selected from the pathway screen
     */
    private fun onPathwayAdbSelected() {
        router.push(Config.DeviceList)
    }

    private fun onPathwayLibrariesSelected() {
        router.push(Config.LibraryList)
    }

    /**
     * Invoked when a device selected
     */
    private fun onDeviceSelected(androidDevice: AndroidDeviceWrapper) {
        router.push(Config.AppList(ApkSource.Adb(androidDevice)))
    }

    /**
     * Invoked when the app got selected
     */
    private fun onAppSelected(
        apkSource: ApkSource<AndroidDeviceWrapper, Account>,
        androidAppWrapper: AndroidAppWrapper
    ) {
        router.push(
            Config.AppDetail(
                apkSource = apkSource,
                androidApp = androidAppWrapper
            )
        )
    }

    /**
     * Invoked when library selected
     */
    private fun onLibrarySelected(libraryWrapper: LibraryWrapper) {
        Desktop.getDesktop().browse(URI(libraryWrapper.website))
    }

    /**
     * Invoked when an update is necessary
     */
    private fun onUpdateNeeded() {
        println("Update needed")
        router.push(Config.Update)
    }

    /**
     * Invoked when back arrow pressed
     */
    private fun onBackClicked() {
        Arbor.d("Back clicked popping")
        router.pop()
    }


}