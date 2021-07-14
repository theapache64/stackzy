package com.theapache64.stackzy.data.repo

import com.squareup.moshi.Moshi
import com.theapache64.stackzy.data.local.GradleInfo
import com.theapache64.stackzy.data.local.GradleInfoJsonAdapter
import com.theapache64.stackzy.data.remote.ApiInterface
import com.theapache64.stackzy.data.remote.Result
import javax.inject.Inject

/**
 * To store/retrieve to/from global Results sheet
 */
class ResultsRepo @Inject constructor(
    private val apiInterface: ApiInterface,
    private val moshi: Moshi
) {
    fun add(result: Result) = apiInterface.addResult(result)
    fun findResult(
        packageName: String,
        versionCode: Int,
        libVersionCode: Int
    ) = apiInterface.getResult(packageName, versionCode, libVersionCode)

    private val gradleInfoAdapter by lazy {
        GradleInfoJsonAdapter(moshi)
    }

    fun parseGradleInfo(gradleInfoJson: String): GradleInfo? {
        return gradleInfoAdapter.fromJson(gradleInfoJson)
    }

    fun jsonify(gradleInfo: GradleInfo): String {
        return gradleInfoAdapter.toJson(gradleInfo)
    }

    fun getAllLibPackages() = apiInterface.getAllLibPackages()
}
