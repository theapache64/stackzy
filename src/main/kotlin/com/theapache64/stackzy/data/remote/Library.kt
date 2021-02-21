package com.theapache64.stackzy.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.theapache64.stackzy.ui.common.AlphabetCircle

@JsonClass(generateAdapter = true)
data class Library(
    @Json(name = "category")
    val category: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "package_name")
    val packageName: String,
    @Json(name = "replacement_package")
    val replacementPackage: String?,
    @Json(name = "website")
    val website: String
) : AlphabetCircle() {

    companion object{
        const val CATEGORY_OTHER = "Other"
    }

    override fun getTitle(): String {
        return name
    }

    override fun getSubtitle(): String {
        return category
    }

}