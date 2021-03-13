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
    val packageName: String, // comma-sep
    @Json(name = "version_code")
    val versionCode: Int,
    @Json(name = "version_name")
    val versionName: String,
    @Json(name = "platform")
    val platform: String,
    @Json(name = "apk_size_in_mb")
    val apkSizeInMb: Float,
    @Json(name = "permissions")
    val permissions: String, // comma-sep
    @Json(name = "gradle_info_json")
    val gradleInfoJson: String,
)