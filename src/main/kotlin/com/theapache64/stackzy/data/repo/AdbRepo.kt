package com.theapache64.stackzy.data.repo

import com.malinskiy.adam.AndroidDebugBridgeClientFactory
import com.malinskiy.adam.interactor.StartAdbInteractor
import com.malinskiy.adam.request.device.AsyncDeviceMonitorRequest
import com.malinskiy.adam.request.device.Device
import com.malinskiy.adam.request.device.DeviceState
import com.malinskiy.adam.request.device.ListDevicesRequest
import com.malinskiy.adam.request.pkg.PmListRequest
import com.malinskiy.adam.request.prop.GetPropRequest
import com.malinskiy.adam.request.shell.v1.ShellCommandRequest
import com.malinskiy.adam.request.sync.v1.PullFileRequest
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import com.theapache64.stackzy.di.AdbFile
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.IOException
import javax.inject.Inject
import kotlin.math.floor
import kotlin.math.roundToInt

class AdbRepo @Inject constructor(
    @AdbFile private val adbFile: File
) {

    companion object {
        const val PATH_PACKAGE_PREFIX = "package:"
        private const val DETAIL_UNKNOWN = "Unknown"
    }

    private var deviceEventsChannel: ReceiveChannel<List<Device>>? = null

    private val startAdbInteractor by lazy {
        StartAdbInteractor()
    }

    private val adb by lazy {
        AndroidDebugBridgeClientFactory()
            .build()
    }

    fun watchConnectedDevice(): Flow<List<AndroidDevice>> {
        return flow {

            val isStarted = startAdbInteractor.execute(adbFile)

            if (isStarted) {
                deviceEventsChannel = adb.execute(
                    request = AsyncDeviceMonitorRequest(),
                    scope = GlobalScope
                )

                adb.execute(request = ListDevicesRequest())

                for (currentDeviceList in deviceEventsChannel!!) {

                    val deviceList = currentDeviceList
                        .filter { it.state == DeviceState.DEVICE }
                        .map { device ->
                            val props = adb.execute(
                                request = GetPropRequest(),
                                serial = device.serial
                            )

                            val deviceProductName = props["ro.product.name"]?.singleLine() ?: DETAIL_UNKNOWN
                            val deviceProductModel = props["ro.product.model"]?.singleLine() ?: DETAIL_UNKNOWN

                            AndroidDevice(
                                deviceProductName,
                                deviceProductModel,
                                device
                            )
                        }

                    // Finally emitting result
                    emit(deviceList)
                }
            } else {
                throw IOException("Failed to start adb")
            }
        }
    }

    fun cancelWatchConnectedDevice() {
        deviceEventsChannel?.cancel()
    }

    /**
     * To get installed app from given device
     */
    suspend fun getInstalledApps(device: Device): List<AndroidApp> {

        val installedPackages = adb.execute(
            request = PmListRequest(
                includePath = false
            ),
            serial = device.serial
        )

        return installedPackages
            .filter { it.name.isNotBlank() }
            .map {
                AndroidApp(it)
            }.apply {
                println("Total apps : $size")
            }
    }


    private fun String.singleLine(): String {
        return this.replace("\n", "")
    }

    /**
     * To get APK path for given app from given device
     */
    suspend fun getApkPath(
        androidDevice: AndroidDevice,
        androidApp: AndroidApp
    ): String? {
        val cmd = "pm path ${androidApp.appPackage.name}"
        val response = adb.execute(
            request = ShellCommandRequest(
                cmd
            ),
            serial = androidDevice.device.serial
        )

        return response.output
            .split("\n")[0] // first file only
            .takeIf { it.contains(PATH_PACKAGE_PREFIX) }
            ?.replace(PATH_PACKAGE_PREFIX, "")?.trim()
    }

    suspend fun pullFile(
        androidDevice: AndroidDevice,
        apkRemotePath: String,
        destinationFile: File
    ): Flow<Int> {
        return flow {
            val channel = adb.execute(
                serial = androidDevice.device.serial,
                request = PullFileRequest(
                    apkRemotePath,
                    destinationFile
                ),
                scope = GlobalScope,
            )

            var percentage: Int? = null
            for (percentageDouble in channel) {
                percentage = floor(percentageDouble * 100).roundToInt()
                emit(percentage)
            }

            if (percentage == null || percentage < 100) {
                throw IOException("TSH : Percentage should be 100 but found $percentage")
            }
        }
    }
}


