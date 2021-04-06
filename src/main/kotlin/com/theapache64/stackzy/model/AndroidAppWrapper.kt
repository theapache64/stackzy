package com.theapache64.stackzy.model

import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidAppDefinition

class AndroidAppWrapper(val androidApp: AndroidApp) : AndroidAppDefinition by androidApp, AlphabetCircle() {
    companion object {
        private val titleNegRegEx = "(\\.app|\\.android|\\.beta|\\.com)".toRegex()
    }

    override fun getTitle(): String {
        return appTitle ?: appPackage.name
            .replace(titleNegRegEx, "")
            .split(".").last().capitalize()
    }

    override fun getSubtitle(): String {
        return appPackage.name
    }

    override fun getSubtitle2(): String? {
        return appSize
    }

    override fun imageUrl(): String? = imageUrl

    override fun getAlphabet(): Char {
        return getTitle().first()
    }
}