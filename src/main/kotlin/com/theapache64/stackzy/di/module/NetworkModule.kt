package com.theapache64.stackzy.di.module

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.theapache64.retrosheet.RetrosheetInterceptor
import com.theapache64.stackzy.data.remote.ApiInterface
import dagger.Module
import dagger.Provides
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
class NetworkModule {

    companion object {
        const val TABLE_CATEGORIES = "categories"
    }

    @Provides
    fun provideRetrosheetInterceptor(): RetrosheetInterceptor {
        return RetrosheetInterceptor.Builder()
            .addSheet(
                sheetName = TABLE_CATEGORIES,
                "id", "name"
            )
            .addSheet(
                sheetName = "libraries",
                "id", "name", "package_name", "category", "website"
            )
            .build()
    }

    @Provides
    fun provideOkHttpClient(retrosheetInterceptor: RetrosheetInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(retrosheetInterceptor)
            .build()
    }

    @ExperimentalSerializationApi
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val jsonContentType = MediaType.parse("application/json")!!

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://docs.google.com/spreadsheets/d/1KBxVO5tXySbezBr-9rb2Y3qWo5PCMrvkD1aWQxZRepI/")
            .addConverterFactory(Json.asConverterFactory(jsonContentType))
            .build()
    }

    @Provides
    fun provideApiInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }
}