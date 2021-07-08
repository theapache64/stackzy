package com.theapache64.stackzy.model

import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.local.AndroidAppDefinition
import java.util.*

class AndroidAppWrapper(val androidApp: AndroidApp) : AndroidAppDefinition by androidApp, AlphabetCircle() {
    companion object {
        /**
         * Remove these keywords when to GUESS app name from package name
         */
        private val titleNegRegEx = "(\\.app|\\.android|\\.beta|\\.com)".toRegex()
    }

    override fun getTitle(): String {
        return appTitle ?: appPackage.name
            .replace(titleNegRegEx, "")
            .split(".").last()
            .replaceFirstChar {
                if (it.isLowerCase()) {
                    it.titlecase(Locale.US)
                } else {
                    it.toString()
                }
            }
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