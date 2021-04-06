package com.theapache64.stackzy.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

interface LibraryDefinition {
    val category: String
    val id: Int
    val name: String
    val packageName: String
    val replacementPackage: String?
    val website: String
}

@JsonClass(generateAdapter = true)
class Library(
    @Json(name = "category")
    override val category: String,
    @Json(name = "id")
    override val id: Int,
    @Json(name = "name")
    override val name: String,
    @Json(name = "package_name")
    override val packageName: String,
    @Json(name = "replacement_package")
    override val replacementPackage: String?,
    @Json(name = "website")
    override val website: String,
) : LibraryDefinition {

    companion object {
        const val CATEGORY_OTHER = "Other"
    }
}