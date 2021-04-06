package com.theapache64.stackzy.util

import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.data.repo.LibrariesRepo
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.toxicbakery.logging.Arbor
import kotlinx.coroutines.flow.collect

suspend fun LibrariesRepo.loadLibs(onLibsLoaded: suspend (List<Library>) -> Unit) {
    getRemoteLibraries().collect {
        when (it) {
            is Resource.Loading -> {
                Arbor.d("Loading libs")
            }
            is Resource.Success -> {
                onLibsLoaded(it.data)
            }
            is Resource.Error -> {
                throw IllegalArgumentException(it.errorData)
            }
        }
    }
}