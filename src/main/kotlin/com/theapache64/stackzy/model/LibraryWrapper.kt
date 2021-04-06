package com.theapache64.stackzy.model

import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.data.remote.LibraryDefinition

class LibraryWrapper(
    private val library: Library
) : AlphabetCircle(), LibraryDefinition by library {


    override fun getTitle(): String {
        return name
    }

    override fun getSubtitle(): String {
        return category
    }

    override fun imageUrl(): String? = null
}