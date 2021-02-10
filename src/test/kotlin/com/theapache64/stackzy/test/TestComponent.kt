package com.theapache64.stackzy.test

import com.theapache64.stackzy.data.remote.ApiInterface
import com.theapache64.stackzy.data.repo.AdbRepo
import com.theapache64.stackzy.data.repo.CategoriesRepo
import com.theapache64.stackzy.data.repo.LibrariesRepo
import com.theapache64.stackzy.di.module.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface TestComponent {
    fun apiInterface(): ApiInterface
    fun categoriesRepo(): CategoriesRepo
    fun librariesRepo(): LibrariesRepo
    fun adbRepo() : AdbRepo
}