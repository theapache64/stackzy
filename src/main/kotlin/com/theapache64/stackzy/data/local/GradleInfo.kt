package com.theapache64.stackzy.data.local

data class GradleInfo(
    val versionCode: String?,
    val versionName: String?,
    val minSdk: Pair<Int, String?>?,
    val targetSdk: Pair<Int, String?>?,
)
