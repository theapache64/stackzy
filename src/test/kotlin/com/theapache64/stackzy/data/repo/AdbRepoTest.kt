package com.theapache64.stackzy.data.repo

import com.malinskiy.adam.request.pkg.Package
import com.theapache64.expekt.should
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.test.FLUTTER_PACKAGE_NAME
import com.theapache64.stackzy.test.MyDaggerMockRule
import com.theapache64.stackzy.test.NATIVE_KOTLIN_PACKAGE_NAME
import com.theapache64.stackzy.test.runBlockingUnitTest
import it.cosenonjaviste.daggermock.InjectFromComponent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.BeforeAll


class AdbRepoTest {
    @get:Rule
    val daggerMockRule = MyDaggerMockRule()

    @InjectFromComponent
    private lateinit var adbRepo: AdbRepo

    @BeforeAll
    @Test
    fun beforeAll() = runBlockingUnitTest {
        val devices = adbRepo.watchConnectedDevice().first()
        assert(devices.isNotEmpty()) {
            "No device found. Please connect a device to run this test"
        }
    }

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
            //verify both system apps and 3rd apps are available
            val (systemApps, thirdPartyApps) = installedApps.partition { it.isSystemApp }
            systemApps.size.should.above(0)
            thirdPartyApps.size.should.above(0)

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
                Package(NATIVE_KOTLIN_PACKAGE_NAME),
                isSystemApp = false,
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
                Package(FLUTTER_PACKAGE_NAME),
                isSystemApp = false,
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
                Package(app),
                isSystemApp = false,
            )
        )
        apkPath.should.`null`
    }

    @Test
    fun `Download ADB works`() = runBlockingUnitTest {
        var lastProgress = 0
        adbRepo.downloadAdb()
            .distinctUntilChanged()
            .collect {
                lastProgress = it
            }

        lastProgress.should.equal(100)
    }
}