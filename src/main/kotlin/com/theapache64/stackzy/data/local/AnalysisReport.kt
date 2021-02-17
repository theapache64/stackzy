package com.theapache64.stackzy.data.local

import com.theapache64.stackzy.data.remote.Library

data class AnalysisReport(
    val appName: String?,
    val packageName: String,
    val platform: Platform,
    // key = Category
    val libraries: Map<String, List<Library>>,
    val untrackedLibraries: Set<String>
)