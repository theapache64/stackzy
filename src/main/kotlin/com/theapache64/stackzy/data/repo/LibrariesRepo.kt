package com.theapache64.stackzy.data.repo

import com.theapache64.stackzy.data.remote.ApiInterface
import com.theapache64.stackzy.data.remote.Library
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibrariesRepo @Inject constructor(
    private val apiInterface: ApiInterface
) {

    private var cachedLibraries: List<Library>? = null

    fun getRemoteLibraries() = apiInterface.getLibraries()

    fun cacheLibraries(Libraries: List<Library>) {
        cachedLibraries = Libraries
    }

    fun getCachedLibraries(): List<Library>? {
        return cachedLibraries
    }
}