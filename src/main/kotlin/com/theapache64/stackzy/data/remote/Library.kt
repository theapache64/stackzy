package com.theapache64.stackzy.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Library(
    @SerialName("category")
    val category: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("package_name")
    val packageName: String,
    @SerialName("website")
    val website: String
)