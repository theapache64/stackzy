package com.theapache64.stackzy.data.repo

import com.malinskiy.adam.AndroidDebugBridgeClientFactory
import com.malinskiy.adam.interactor.StartAdbInteractor
import com.malinskiy.adam.request.device.AsyncDeviceMonitorRequest
import com.malinskiy.adam.request.device.Device
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AdbRepo @Inject constructor() {

    private var deviceEventsChannel: ReceiveChannel<List<Device>>? = null

    private val startAdbInteractor by lazy {
        StartAdbInteractor()
    }

    private val adb by lazy {
        AndroidDebugBridgeClientFactory().build()
    }

    fun watchConnectedDevice(): Flow<List<Device>> {
        return flow {
            val isAdbStarted = startAdbInteractor.execute()
            if (isAdbStarted) {

                deviceEventsChannel = adb.execute(
                    request = AsyncDeviceMonitorRequest(),
                    scope = GlobalScope
                )

                for (currentDeviceList in deviceEventsChannel!!) {
                    emit(currentDeviceList)
                }
            }
        }
    }

    fun cancelWatchConnectedDevice() {
        deviceEventsChannel?.cancel()
    }


    /*suspend fun runIt() {
        val result = startAdbInteractor.execute()
        println("Result: $result")
        val adb = AndroidDebugBridgeClientFactory().build()
        println("Dev: ${adb.execute(ListDevicesRequest())}")

        val deviceEventsChannel: ReceiveChannel<List<Device>> = adb.execute(
            request = AsyncDeviceMonitorRequest(),
            scope = GlobalScope
        )

        for (currentDeviceList in deviceEventsChannel) {
            println("List :${currentDeviceList}")
        }


        deviceEventsChannel.cancel()
    }

    init {

    }*/

}