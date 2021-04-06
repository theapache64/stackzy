package com.theapache64.stackzy.data.repo

import com.theapache64.stackzy.data.remote.ApiInterface
import com.theapache64.stackzy.data.remote.UntrackedLibrary
import javax.inject.Inject

/**
 * A repository to fetch and insert data from/to the untracked_libs table
 */
class UntrackedLibsRepo @Inject constructor(
    private val apiInterface: ApiInterface
) {
    fun add(untrackedLibrary: UntrackedLibrary) = apiInterface.addUntrackedLibrary(untrackedLibrary)
    fun getUntrackedLibs() = apiInterface.getUntrackedLibraries()
}