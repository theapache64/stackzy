package com.theapache64.stackzy.data.remote

import com.theapache64.retrosheet.core.Read
import com.theapache64.stackzy.di.module.NetworkModule
import com.theapache64.stackzy.utils.calladapter.flow.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface ApiInterface {

    @Read("SELECT * ")
    @GET(NetworkModule.TABLE_CATEGORIES)
    fun getCategories(): Flow<Resource<List<Category>>>

    @Read("SELECT * ")
    @GET(NetworkModule.TABLE_LIBRARIES)
    fun getLibraries(): Flow<Resource<List<Library>>>

}