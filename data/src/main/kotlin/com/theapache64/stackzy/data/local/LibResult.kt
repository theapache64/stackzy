package com.theapache64.stackzy.data.local

import com.theapache64.stackzy.data.remote.Library

data class LibResult(
    var appLibs: Set<Library>,
    var transitiveDeps: Set<Library>,
    val untrackedLibs: Set<String>,
)