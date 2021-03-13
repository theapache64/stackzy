package com.theapache64.stackzy.data.local

import com.malinskiy.adam.request.pkg.Package
import com.theapache64.stackzy.ui.common.AlphabetCircle

class AndroidApp(
    val appPackage: Package,
    val versionCode: Long? = null,
    val appTitle: String? = null,
    val imageUrl: String? = null,
    val appSize: String? = null
) : AlphabetCircle() {

    override fun getTitle(): String {
        return appTitle ?: appPackage.name.split(".").last().capitalize()
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