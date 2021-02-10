package com.theapache64.stackzy.data.repo

import com.malinskiy.adam.AndroidDebugBridgeClientFactory
import com.malinskiy.adam.interactor.StartAdbInteractor
import com.malinskiy.adam.request.device.ListDevicesRequest
import javax.inject.Inject

class AdbRepo @Inject constructor() {
    suspend fun runIt() {
        val result = StartAdbInteractor().execute()
        println("Result: $result")
        val adb = AndroidDebugBridgeClientFactory().build()
        println("Dev: ${adb.execute(ListDevicesRequest())}")
    }

    init {

    }
}