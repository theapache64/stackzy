package com.theapache64.stackzy.model

import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.data.remote.LibraryDefinition
import com.theapache64.stackzy.data.remote.Result

class LibraryWrapper(
    private val library: Library,
    private val prevResult: Result?
) : AlphabetCircle(), LibraryDefinition by library {

    private val isNewLib: Boolean by lazy {
        if (prevResult != null) {
            // If the prev result has this library inside the libPackage, then it is not a new lib.
            prevResult.libPackages?.contains(library.packageName) == false
        } else {
            false
        }
    }

    override fun getTitle(): String {
        return name
    }

    override fun getSubtitle(): String {
        return category
    }

    override fun imageUrl(): String? = null

    override fun isNew(): Boolean {
        return isNewLib
    }
}