package com.theapache64.stackzy.data.remote

import com.github.theapache64.retrosheet.core.KeyValue
import com.github.theapache64.retrosheet.core.Read
import com.github.theapache64.retrosheet.core.Write
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.theapache64.stackzy.di.module.NetworkModule
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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

    @KeyValue
    @GET(NetworkModule.TABLE_CONFIG)
    fun getConfig(): Flow<Resource<Config>>

    @Write
    @POST(NetworkModule.TABLE_RESULTS)
    fun addResult(@Body result: Result): Flow<Resource<Result>>

    @Read("SELECT * WHERE package_name = :package_name AND version_code = :version_code AND stackzy_lib_version = :stackzy_lib_version LIMIT 1")
    @GET(NetworkModule.TABLE_RESULTS)
    fun getResult(
        @Query("package_name") packageName: String,
        @Query("version_code") versionCode: Int,
        @Query("stackzy_lib_version") stackzyLibVersion: Int,
    ): Flow<Resource<Result>>

    @Read("SELECT lib_packages WHERE lib_packages != ''")
    @GET(NetworkModule.TABLE_RESULTS)
    fun getAllLibPackages(): Flow<Resource<List<OptionalResult>>>

}