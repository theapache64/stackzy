package com.theapache64.stackzy.data.repo

import com.malinskiy.adam.AndroidDebugBridgeClientFactory
import com.malinskiy.adam.interactor.StartAdbInteractor
import com.malinskiy.adam.request.device.AsyncDeviceMonitorRequest
import com.malinskiy.adam.request.device.Device
import com.malinskiy.adam.request.device.DeviceState
import com.malinskiy.adam.request.device.ListDevicesRequest
import com.malinskiy.adam.request.pkg.PmListRequest
import com.malinskiy.adam.request.prop.GetPropRequest
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidDevice
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AdbRepo @Inject constructor() {

    companion object {
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
            val isAdbStarted = startAdbInteractor.execute()
            if (isAdbStarted) {

                deviceEventsChannel = adb.execute(
                    request = AsyncDeviceMonitorRequest(),
                    scope = GlobalScope
                )

                adb.execute(
                    request = ListDevicesRequest()
                )

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
            }
        }
    }

    fun cancelWatchConnectedDevice() {
        deviceEventsChannel?.cancel()
    }

    suspend fun getInstalledApps(device: Device): List<AndroidApp> {

        val isAdbStarted = startAdbInteractor.execute()
        if (isAdbStarted) {

            val installedPackages = adb.execute(
                request = PmListRequest(
                    includePath = false
                ),
                serial = device.serial
            )

            return installedPackages
                .filter { it.name.isNotBlank() }
                .map {
                    println(it.name)
                    AndroidApp(it)
                }
        }

        return listOf()
    }

}

private fun String.singleLine(): String {
    return this.replace("\n", "")
}
