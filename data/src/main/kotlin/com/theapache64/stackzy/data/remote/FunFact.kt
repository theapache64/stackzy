package com.theapache64.stackzy.data.remote
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class FunFact(
    @Json(name = "id")
    val id: Int, // 1
    @Json(name = "fun_fact")
    val funFact: String // lorem ipsum
)