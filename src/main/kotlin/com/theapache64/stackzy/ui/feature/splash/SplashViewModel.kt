package com.theapache64.stackzy.ui.feature.splash

import com.theapache64.stackzy.data.repo.CategoriesRepo
import com.theapache64.stackzy.utils.calladapter.flow.Resource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val categoriesRepo: CategoriesRepo
) {

    private val _isSyncFinished = MutableStateFlow(false)
    val isSyncFinished: StateFlow<Boolean> = _isSyncFinished

    private val _isSyncFailed = MutableStateFlow<String?>(null)
    val isSyncFailed: StateFlow<String?> = _isSyncFailed

    init {
        syncData()
    }

    private fun syncData() {
        GlobalScope.launch {
            categoriesRepo.getCategories()
                .collect {
                    when (it) {
                        is Resource.Loading -> {
                            _isSyncFinished.value = false
                            _isSyncFailed.value = null
                        }
                        is Resource.Success -> {
                            _isSyncFinished.value = true
                        }
                        is Resource.Error -> {
                            _isSyncFailed.value = it.errorData
                        }
                    }
                }
        }
    }

    fun onRetryClicked() {
        syncData()
    }
}