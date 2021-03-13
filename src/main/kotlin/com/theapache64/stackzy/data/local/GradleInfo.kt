package com.theapache64.stackzy.data.local

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GradleInfo(
    val versionCode: Long?,
    val versionName: String?,
    val minSdk: Sdk?,
    val targetSdk: Sdk?,
) {
    data class Sdk(
        val sdkInt: Int,
        val versionName: String?
    )
}
