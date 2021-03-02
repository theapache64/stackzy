package com.theapache64.stackzy.data.local

import com.theapache64.stackzy.data.remote.Library
import java.io.File


data class AnalysisReport(
    val appName: String?,
    val packageName: String,
    val platform: Platform,
    // key = Category
    val libraries: List<Library>,
    val untrackedLibraries: Set<String>,
    val apkSizeInMb: Double,
    val assetsDir: File?,
    val permissions: List<String>,
    val gradleInfo: GradleInfo
)