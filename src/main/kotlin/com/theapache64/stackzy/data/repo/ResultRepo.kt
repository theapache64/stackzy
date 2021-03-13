package com.theapache64.stackzy.data.repo

import com.theapache64.stackzy.data.remote.ApiInterface
import com.theapache64.stackzy.data.remote.Result
import javax.inject.Inject

class ResultRepo @Inject constructor(
    private val apiInterface: ApiInterface
) {
    fun add(result: Result) = apiInterface.addResult(result)
    fun findResult(packageName: String, versionCode: Long) = apiInterface.getResult(packageName, versionCode)

}