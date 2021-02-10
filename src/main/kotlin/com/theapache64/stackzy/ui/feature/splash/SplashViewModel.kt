package com.theapache64.stackzy.ui.feature.splash

import com.theapache64.stackzy.data.repo.CategoriesRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val categoriesRepo: CategoriesRepo
) {
    init {
        GlobalScope.launch {
            categoriesRepo.getCategories().collect {
                println(it)
            }
        }
    }
}