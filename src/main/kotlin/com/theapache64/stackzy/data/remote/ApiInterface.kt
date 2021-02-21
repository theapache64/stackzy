package com.theapache64.stackzy.data.remote

import com.theapache64.retrosheet.core.Read
import com.theapache64.retrosheet.core.Write
import com.theapache64.stackzy.di.module.NetworkModule
import com.theapache64.stackzy.utils.calladapter.flow.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

    @Read("SELECT * ")
    @GET(NetworkModule.TABLE_LIBRARIES)
    fun getLibraries(): Flow<Resource<List<Library>>>

    @Write
    @POST(NetworkModule.TABLE_UNTRACKED_LIBS)
    fun addUntrackedLibrary(
        @Body untrackedLibrary: UntrackedLibrary
    ): Flow<Resource<UntrackedLibrary>>

    @Read("SELECT *")
    @GET(NetworkModule.TABLE_UNTRACKED_LIBS)
    fun getUntrackedLibraries(): Flow<Resource<List<UntrackedLibrary>>>

}