package com.theapache64.stackzy.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OptionalResult(
    @Json(name = "app_libs")
    val appLibs: String?,
)
