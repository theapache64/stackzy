package com.theapache64.stackzy.data.repo

import com.malinskiy.adam.request.pkg.Package
import com.theapache64.expekt.should
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.test.FLUTTER_PACKAGE_NAME
import com.theapache64.stackzy.test.MyDaggerMockRule
import com.theapache64.stackzy.test.NATIVE_KOTLIN_PACKAGE_NAME
import com.theapache64.stackzy.test.runBlockingUnitTest
import it.cosenonjaviste.daggermock.InjectFromComponent
import kotlinx.coroutines.flow.first
import org.junit.Rule
import org.junit.Test


class AdbRepoTest {
    @get:Rule
    val daggerMockRule = MyDaggerMockRule()

    @InjectFromComponent
    private lateinit var adbRepo: AdbRepo

    @Test
    fun `Device list works`() = runBlockingUnitTest {
        val connectedDevices = adbRepo.watchConnectedDevice().first()
        if (connectedDevices.isEmpty()) {
            assert(false) {
                "Are you sure you've at least one device connected"
            }
        } else {
            // not empty. one or more devices are connected
            assert(true)
        }
    }

    @Test
    fun `App list works`() = runBlockingUnitTest {
        val connectedDevices = adbRepo.watchConnectedDevice().first()
        val installedApps = adbRepo.getInstalledApps(connectedDevices.first().device)
        if (installedApps.isEmpty()) {
            assert(false) {
                "Are you sure you've at least one device connected"
            }
        } else {
            // not empty. one or more devices are connected
            assert(true)
        }
    }

    @Test
    fun `Fetch path works - native android`() = runBlockingUnitTest {
        val device = adbRepo.watchConnectedDevice().first().first()
        val apkPath = adbRepo.getApkPath(
            device,
            AndroidApp(
                Package(NATIVE_KOTLIN_PACKAGE_NAME)
            )
        )
        apkPath.should.startWith("/data/app/")
    }

    @Test
    fun `Fetch path works - flutter`() = runBlockingUnitTest {
        val device = adbRepo.watchConnectedDevice().first().first()
        val apkPath = adbRepo.getApkPath(
            device,
            AndroidApp(
                Package(FLUTTER_PACKAGE_NAME)
            )
        )
        apkPath.should.startWith("/data/app/")
    }

    @Test
    fun `Fetch path fails for invalid package`() = runBlockingUnitTest {
        val device = adbRepo.watchConnectedDevice().first().first()
        val app = "dffgfgdf"
        val apkPath = adbRepo.getApkPath(
            device,
            AndroidApp(
                Package(app)
            )
        )
        apkPath.should.`null`
    }
}