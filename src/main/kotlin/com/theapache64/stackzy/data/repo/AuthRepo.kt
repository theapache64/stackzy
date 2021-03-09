package com.theapache64.stackzy.data.repo

import com.squareup.moshi.Moshi
import com.theapache64.gpa.model.Account
import java.util.prefs.Preferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepo @Inject constructor(
    private val moshi: Moshi
) {

    companion object {
        private const val KEY_ACCOUNT = "account"
    }

    private val accountAdapter by lazy {
        moshi.adapter(Account::class.java)
    }

    private val pref by lazy {
        Preferences.userRoot().node(AuthRepo::class.java.simpleName)
    }

    /**
     * To get active account
     */
    fun getAccount(): Account? {
        val accountJson = pref.get(KEY_ACCOUNT, null)
        return if (accountJson != null) {
            // Parse
            accountAdapter.fromJson(accountJson)
        } else {
            null
        }
    }

}