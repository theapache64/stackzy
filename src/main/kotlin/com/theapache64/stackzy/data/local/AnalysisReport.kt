package com.theapache64.stackzy.data.local

import com.theapache64.stackzy.data.remote.Category
import com.theapache64.stackzy.data.remote.Library

data class AnalysisReport(
    val appName: String?,
    val packageName: String,
    val platform: Platform,
    // key = Category
    val libraries: Map<String, List<Library>>,
    val untrackedLibraries: Set<String>
) {

    val allLibraries by lazy {
        val libsUsed = mutableListOf<Library>()
        for (x in libraries.values) {
            libsUsed.addAll(x)
        }

        // Sort : Other libs should be last
        libsUsed.sortedBy { it.category == Category.OTHER }
    }
}