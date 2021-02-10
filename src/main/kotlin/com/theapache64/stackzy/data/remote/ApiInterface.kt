package com.theapache64.stackzy.data.remote

import com.theapache64.cyclone.core.network.Resource
import com.theapache64.retrosheet.core.Read
import com.theapache64.stackzy.di.module.NetworkModule
import retrofit2.http.GET

interface ApiInterface {

    @Read("SELECT * ")
    @GET(NetworkModule.TABLE_CATEGORIES)
    fun getCategories(): Resource<List<Category>>

}