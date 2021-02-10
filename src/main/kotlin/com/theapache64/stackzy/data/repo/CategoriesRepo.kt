package com.theapache64.stackzy.data.repo

import com.theapache64.stackzy.data.remote.ApiInterface
import com.theapache64.stackzy.data.remote.Category
import javax.inject.Inject

/**
 * To manage library categories
 */
class CategoriesRepo @Inject constructor(
    private val apiInterface: ApiInterface
) {
    private var cachedCategories: List<Category>? = null

    fun getRemoteCategories() = apiInterface.getCategories()

    fun cacheCategories(categories: List<Category>) {
        this.cachedCategories = categories
    }

    fun getCachedCategories() = cachedCategories
}