package com.theapache64.stackzy.data.repo

import com.malinskiy.adam.AndroidDebugBridgeClientFactory
import com.malinskiy.adam.interactor.StartAdbInteractor
import com.malinskiy.adam.request.device.AsyncDeviceMonitorRequest
import com.malinskiy.adam.request.device.Device
import com.malinskiy.adam.request.device.ListDevicesRequest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import javax.inject.Inject

class AdbRepo @Inject constructor() {

    init {

    }

    suspend fun runIt() {
        val result = StartAdbInteractor().execute()
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


    }

    init {

    }

}