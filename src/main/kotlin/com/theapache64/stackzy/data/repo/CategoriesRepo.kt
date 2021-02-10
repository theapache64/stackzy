package com.theapache64.stackzy.data.repo

import com.theapache64.stackzy.data.remote.ApiInterface
import javax.inject.Inject

class CategoriesRepo @Inject constructor(
    private val apiInterface: ApiInterface
) {
    fun getCategories() = apiInterface.getCategories()
}