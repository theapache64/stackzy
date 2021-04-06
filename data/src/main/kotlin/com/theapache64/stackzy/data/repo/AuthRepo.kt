package com.theapache64.stackzy.data.repo

import com.github.theapache64.gpa.api.Play
import com.github.theapache64.gpa.model.Account
import com.squareup.moshi.Moshi
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.theapache64.stackzy.util.Crypto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.prefs.Preferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepo @Inject constructor(
    private val moshi: Moshi,
    private val pref: Preferences,
    private val crypto: Crypto
) {

    companion object {
        private const val KEY_ENC_ACCOUNT = "enc_account"
    }

    private val accountAdapter by lazy {
        moshi.adapter(Account::class.java)
    }

    /**
     * To get active account
     */
    fun getAccount(): Account? {
        val encAccountJson = pref.get(KEY_ENC_ACCOUNT, null)
        return if (encAccountJson != null) {
            // Parse
            val accountJson = crypto.decrypt(encAccountJson)
            accountAdapter.fromJson(accountJson)
        } else {
            null
        }
    }

    /**
     * To login with given google username and password
     */
    suspend fun logIn(username: String, password: String) = withContext(Dispatchers.IO) {
        flow<Resource<Account>> {
            // Loading
            emit(Resource.Loading())

            try {
                val account = Play.login(username, password)
                emit(Resource.Success(null, account))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "Something went wrong"))
            }
        }
    }

    /**
     * To persist account
     */
    fun storeAccount(account: Account) {
        val encAccountJson = crypto.encrypt(accountAdapter.toJson(account))
        pref.put(KEY_ENC_ACCOUNT, encAccountJson)
    }

    /**
     * To logout and remove account details from preference
     */
    fun logout() {
        pref.remove(KEY_ENC_ACCOUNT)
    }

    /**
     * To get account or throw exception
     */
    fun getAccountOrThrow() = getAccount()
        ?: throw IllegalArgumentException("Couldn't get account. Are you sure you've logged in via the app?")


}