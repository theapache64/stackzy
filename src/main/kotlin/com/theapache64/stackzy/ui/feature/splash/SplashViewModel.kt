package com.theapache64.stackzy.ui.feature.splash

import com.theapache64.stackzy.data.repo.CategoriesRepo
import com.theapache64.stackzy.utils.calladapter.flow.Resource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val categoriesRepo: CategoriesRepo
) {

    private val _isSyncFinished = MutableStateFlow(false)
    val isSyncFinished: StateFlow<Boolean> = _isSyncFinished

    init {
        GlobalScope.launch {
            categoriesRepo.getCategories()
                .onEach {
                    delay(2000)
                }
                .collect {
                    when (it) {
                        is Resource.Success -> {
                            _isSyncFinished.value = true
                        }
                    }
                }
        }
    }
}