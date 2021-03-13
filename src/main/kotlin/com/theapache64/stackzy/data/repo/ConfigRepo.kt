package com.theapache64.stackzy.data.repo

import com.squareup.moshi.Moshi
import com.theapache64.stackzy.data.remote.ApiInterface
import com.theapache64.stackzy.data.remote.Config
import com.theapache64.stackzy.data.remote.ConfigJsonAdapter
import java.util.prefs.Preferences
import javax.inject.Inject
import javax.inject.Singleton

/**
 * To manage global config flags
 */
@Singleton
open class ConfigRepo @Inject constructor(
    private val apiInterface: ApiInterface,
    private val moshi: Moshi,
    private val pref: Preferences
) {
    companion object {
        private const val KEY_CONFIG = "config"
    }

    private val configJsonAdapter by lazy {
        ConfigJsonAdapter(moshi)
    }

    fun getRemoteConfig() =
        apiInterface.getConfig()

    fun getLocalConfig(): Config? {
        return pref.get(KEY_CONFIG, null)?.run {
            configJsonAdapter.fromJson(this)
        }
    }

    fun saveConfigToLocal(data: Config) {
        pref.put(KEY_CONFIG, configJsonAdapter.toJson(data))
    }

}