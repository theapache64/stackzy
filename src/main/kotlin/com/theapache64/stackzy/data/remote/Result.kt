package com.theapache64.stackzy.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Result(
    @Json(name = "app_name")
    val appName: String,
    @Json(name = "lib_packages")
    val libPackages: String,
    @Json(name = "package_name")
    val packageName: String,
    @Json(name = "version_code")
    val versionCode: Int,
    @Json(name = "version_name")
    val versionName: String
)