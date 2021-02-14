package com.theapache64.stackzy.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class UntrackedLibrary(
    @Json(name = "package_name")
    val packageName: String
)