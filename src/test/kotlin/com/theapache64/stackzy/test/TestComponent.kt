package com.theapache64.stackzy.test

import com.theapache64.stackzy.data.remote.ApiInterface
import com.theapache64.stackzy.data.repo.*
import com.theapache64.stackzy.di.module.ApkToolModule
import com.theapache64.stackzy.di.module.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, ApkToolModule::class, AdbModule::class])
interface TestComponent {
    fun apiInterface(): ApiInterface
    fun librariesRepo(): LibrariesRepo
    fun adbRepo(): AdbRepo
    fun apkToolRepo(): ApkToolRepo
    fun apkAnalyzerRepo(): ApkAnalyzerRepo
    fun untrackedLibsRepo(): UntrackedLibsRepo
}