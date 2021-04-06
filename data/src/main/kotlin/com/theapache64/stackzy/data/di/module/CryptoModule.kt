package com.theapache64.stackzy.data.di.module

import com.theapache64.stackzy.util.Crypto
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CryptoModule {
    companion object {
        private val SALT = "tHeApAChe64Stack".toByteArray()
    }

    @Singleton
    @Provides
    fun provideCrypto(): Crypto {
        return Crypto(SALT)
    }
}