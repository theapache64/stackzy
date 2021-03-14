package com.theapache64.stackzy.data.local

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppArgs(
    @Json(name = "app_name")
    val appName: String,
    @Json(name = "version")
    val version: String
)