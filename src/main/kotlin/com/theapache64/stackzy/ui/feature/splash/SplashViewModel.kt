package com.theapache64.stackzy.ui.feature.splash

import com.theapache64.stackzy.data.repo.CategoriesRepo
import com.theapache64.stackzy.data.repo.LibrariesRepo
import com.theapache64.stackzy.util.R
import com.theapache64.stackzy.utils.calladapter.flow.Resource
import com.toxicbakery.logging.Arbor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val categoriesRepo: CategoriesRepo,
    private val librariesRepo: LibrariesRepo
) {

    private val _isSyncFinished = MutableStateFlow(false)
    val isSyncFinished: StateFlow<Boolean> = _isSyncFinished

    private val _isSyncFailed = MutableStateFlow<String?>(null)
    val isSyncFailed: StateFlow<String?> = _isSyncFailed

    init {
        syncData()
    }

    /**
     * To sync remote data with local
     */
    private fun syncData() {
        GlobalScope.launch {
            try {
                categoriesRepo.getRemoteCategories()
                    .zip(librariesRepo.getRemoteLibraries()) { r1, r2 ->

                        if (r1 is Resource.Loading && r2 is Resource.Loading) {
                            // Both request are loading
                            _isSyncFinished.value = false
                            _isSyncFailed.value = null
                        } else if (r1 is Resource.Success && r2 is Resource.Success) {
                            // Both requests succeeded

                            // Cache categories
                            categoriesRepo.cacheCategories(r1.data)

                            // Cache libraries
                            librariesRepo.cacheLibraries(r2.data)

                            Arbor.d("${categoriesRepo.getCachedCategories()?.size} categories cached")
                            Arbor.d("${librariesRepo.getCachedLibraries()?.size} libraries cached")

                            // delay(1000)
                            _isSyncFinished.value = true
                        } else {
                            _isSyncFailed.value = when {
                                r1 is Resource.Error -> {
                                    r1.errorData
                                }
                                r2 is Resource.Error -> {
                                    r2.errorData
                                }
                                else -> {
                                    R.string.all_error_unknown
                                }
                            }
                        }

                    }
                    .collect()

            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                _isSyncFailed.value = e.message
            }
        }
    }

    fun onRetryClicked() {
        syncData()
    }
}