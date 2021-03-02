package com.theapache64.stackzy.di.module

import com.theapache64.retrosheet.RetrosheetInterceptor
import com.theapache64.stackzy.data.remote.ApiInterface
import com.theapache64.stackzy.utils.calladapter.flow.FlowResourceCallAdapterFactory
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
            .addForm(
                TABLE_UNTRACKED_LIBS,
                "https://docs.google.com/forms/d/e/1FAIpQLSdWuRkjXqBkL-w5NfktA_ju_sI2bJTDVb4LoYco4mxEpskU9g/viewform?usp=sf_link"
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
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://docs.google.com/spreadsheets/d/1KBxVO5tXySbezBr-9rb2Y3qWo5PCMrvkD1aWQxZRepI/")
            .addConverterFactory(MoshiConverterFactory.create())
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