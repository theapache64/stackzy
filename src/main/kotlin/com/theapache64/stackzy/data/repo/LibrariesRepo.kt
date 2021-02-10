package com.theapache64.stackzy.data.repo

import com.theapache64.stackzy.data.remote.ApiInterface
import com.theapache64.stackzy.data.remote.Library
import javax.inject.Inject

class LibrariesRepo @Inject constructor(
    private val apiInterface: ApiInterface
) {
    private var cachedLibraries: List<Library>? = null

    fun getRemoteLibraries() = apiInterface.getLibraries()

    fun cacheLibraries(Libraries: List<Library>) {
        this.cachedLibraries = Libraries
    }

    fun getCachedLibraries() = cachedLibraries
}