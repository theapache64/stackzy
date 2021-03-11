package com.theapache64.stackzy.util.calladapter.flow

/**
 * Created by theapache64 : Jul 26 Sun,2020 @ 13:22
 */
sealed class Resource<T> {

    class Loading<T> : Resource<T>()

    data class Success<T>(
        val message: String?,
        val data: T
    ) : Resource<T>()

    data class Error<T>(
        val errorData: String
    ) : Resource<T>()
}
