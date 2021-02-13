package com.theapache64.stackzy.data.repo

import com.theapache64.stackzy.data.remote.ApiInterface
import com.theapache64.stackzy.data.remote.Library
import com.toxicbakery.logging.Arbor
import javax.inject.Inject

class LibrariesRepo @Inject constructor(
    private val apiInterface: ApiInterface
) {
    init {
        Arbor.i("New Instance : $this")
    }

    private var cachedLibraries: List<Library>? = null

    fun getRemoteLibraries() = apiInterface.getLibraries()

    fun cacheLibraries(Libraries: List<Library>) {
        println("Caching libs @ ${this}")
        this.cachedLibraries = Libraries
    }

    fun getCachedLibraries(): List<Library>? {
        println("Getting cached libs from ${this}")
        return cachedLibraries
    }
}