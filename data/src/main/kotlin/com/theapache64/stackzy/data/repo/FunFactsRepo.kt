package com.theapache64.stackzy.data.repo

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.theapache64.stackzy.data.remote.ApiInterface
import com.theapache64.stackzy.data.remote.FunFact
import java.util.prefs.Preferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class FunFactsRepo @Inject constructor(
    private val apiInterface: ApiInterface,
    private val moshi: Moshi,
    private val pref: Preferences
) {
    companion object {
        private const val KEY_FUN_FACTS = "FunFacts"
    }

    private val funFactsJsonAdapter by lazy {
        val listMyData = Types.newParameterizedType(List::class.java, FunFact::class.java)
        moshi.adapter<List<FunFact>>(listMyData)
    }

    fun getRemoteFunFacts() =
        apiInterface.getFunFacts()

    fun getLocalFunFacts(): Set<FunFact>? {
        val funFactsJson = pref.get(KEY_FUN_FACTS, null)
        return funFactsJsonAdapter.fromJson(funFactsJson)?.toSet()
    }

    fun saveFunFactsToLocal(data: List<FunFact>) {
        pref.put(KEY_FUN_FACTS, funFactsJsonAdapter.toJson(data))
    }

}
