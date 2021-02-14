package com.theapache64.stackzy.data.local

import com.malinskiy.adam.request.pkg.Package
import com.theapache64.stackzy.ui.common.AlphabetCircle

class AndroidApp(
    val appPackage: Package
) : AlphabetCircle() {

    override fun getTitle(): String {
        return appPackage.name.split(".").last().capitalize()
    }

    override fun getSubtitle(): String {
        return appPackage.name
    }

    override fun getAlphabet(): Char {
        return getTitle().first()
    }
}