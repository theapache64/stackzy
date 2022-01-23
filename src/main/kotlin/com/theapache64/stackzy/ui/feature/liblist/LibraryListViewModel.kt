package com.theapache64.stackzy.ui.feature.liblist

import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.data.remote.OptionalResult
import com.theapache64.stackzy.data.repo.LibrariesRepo
import com.theapache64.stackzy.data.repo.ResultsRepo
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.theapache64.stackzy.model.LibraryWrapper
import com.theapache64.stackzy.ui.util.getSingularOrPlural
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class LibraryListViewModel @Inject constructor(
    private val librariesRepo: LibrariesRepo,
    private val resultsRepo: ResultsRepo
) {
    private lateinit var viewModelScope: CoroutineScope

    private var fullLibs: List<LibraryWrapper>? = null
    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword: StateFlow<String> = _searchKeyword

    private val _subTitle = MutableStateFlow("")
    val subTitle: StateFlow<String> = _subTitle

    private val _libsResp = MutableStateFlow<Resource<List<LibraryWrapper>>?>(null)
    val libsResp: StateFlow<Resource<List<LibraryWrapper>>?> = _libsResp

    fun init(
        scope: CoroutineScope,
    ) {
        this.viewModelScope = scope
    }

    fun loadLibraries() {
        viewModelScope.launch {
            val cachedLibs = librariesRepo.getCachedLibraries()!!
            resultsRepo.getAllLibPackages().collect {
                when (it) {
                    is Resource.Loading -> {
                        _libsResp.value = Resource.Loading()
                    }
                    is Resource.Success -> {
                        // Here, valid means results that have at least one library
                        val validLibPackages = filterValidLibPackages(cachedLibs, allLibPackages = it.data)
                        fullLibs = validLibPackages
                        _libsResp.value = Resource.Success(validLibPackages)
                        _subTitle.value = getSubtitleFor(validLibPackages)
                    }
                    is Resource.Error -> {
                        _libsResp.value = Resource.Error(it.errorData)
                    }
                }
            }
        }
    }

    private fun filterValidLibPackages(
        cachedLibs: List<Library>,
        allLibPackages: List<OptionalResult>
    ): List<LibraryWrapper> {
        return cachedLibs.filter { library ->
            allLibPackages.find {
                it.libPackages?.contains(library.packageName) ?: false
            } != null
        }.map { LibraryWrapper(it, null) }
    }

    fun onSearchKeywordChanged(newKeyword: String) {
        _searchKeyword.value = newKeyword.replace("\n", "")

        // Filtering libraries
        val apps = if (newKeyword.isNotBlank()) {
            // Filter
            fullLibs
                ?.filter {
                    // search with keyword
                    it.name.lowercase(Locale.getDefault()).contains(newKeyword, ignoreCase = true) ||
                            it.packageName.lowercase(Locale.getDefault()).contains(newKeyword, ignoreCase = true)
                }
                ?: listOf()
        } else {
            fullLibs ?: listOf()
        }

        _subTitle.value = getSubtitleFor(apps)
        _libsResp.value = Resource.Success(apps)
    }

    private fun getSubtitleFor(apps: List<LibraryWrapper>): String {
        return "${apps.size} ${apps.size.getSingularOrPlural("library", "libraries")}"
    }
}
