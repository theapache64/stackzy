package com.theapache64.stackzy.data.di.module

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.theapache64.retrosheet.RetrosheetInterceptor
import com.theapache64.stackzy.data.remote.ApiInterface
import com.theapache64.stackzy.data.util.calladapter.flow.FlowResourceCallAdapterFactory
import com.toxicbakery.logging.Arbor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
class NetworkModule {

    companion object {
        const val TABLE_CATEGORIES = "categories"
        const val TABLE_LIBRARIES = "libraries"
        const val TABLE_UNTRACKED_LIBS = "untracked_libs"
        const val TABLE_RESULTS = "results"
        const val TABLE_CONFIG = "config"
    }

    @Singleton
    @Provides
    fun provideRetrosheetInterceptor(): RetrosheetInterceptor {
        return RetrosheetInterceptor.Builder()
            .addSheet(
                sheetName = TABLE_CATEGORIES,
                "id", "name"
            )
            .addSheet(
                sheetName = TABLE_LIBRARIES,
                "id", "name", "package_name", "category", "website"
            )
            .addSheet(
                sheetName = TABLE_UNTRACKED_LIBS,
                "created_at", "package_names"
            )
            .addSheet(
                sheetName = TABLE_CONFIG,
                "should_consider_result_cache",
                "current_lib_version_code"
            )
            .addForm(
                TABLE_UNTRACKED_LIBS,
                "https://docs.google.com/forms/d/e/1FAIpQLSdWuRkjXqBkL-w5NfktA_ju_sI2bJTDVb4LoYco4mxEpskU9g/viewform?usp=sf_link"
            )
            .addSheet(
                sheetName = TABLE_RESULTS,
                "created_at",
                "app_name",
                "package_name",
                "version_name",
                "version_code",
                "stackzy_lib_version",
                "platform",
                "apk_size_in_mb",
                "permissions",
                "gradle_info_json",
                "lib_packages"
            )
            .addForm(
                endPoint = TABLE_RESULTS,
                formLink = "https://docs.google.com/forms/d/e/1FAIpQLSdiTZz47N2FHUXLSvsdzAxVRKqzWq30xjkpCOQugKbHLJuRGg/viewform?usp=sf_link"
            )
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(retrosheetInterceptor: RetrosheetInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(retrosheetInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://docs.google.com/spreadsheets/d/1KBxVO5tXySbezBr-9rb2Y3qWo5PCMrvkD1aWQxZRepI/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(FlowResourceCallAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiInterface(retrofit: Retrofit): ApiInterface {
        Arbor.d("Creating new API interface")
        return retrofit.create(ApiInterface::class.java)
    }
}