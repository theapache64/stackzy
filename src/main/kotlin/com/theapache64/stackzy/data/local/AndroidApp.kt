package com.theapache64.stackzy.data.local

import com.malinskiy.adam.request.pkg.Package
import com.theapache64.stackzy.ui.common.Selectable

class AndroidApp(
    val appPackage: Package
) : Selectable {

    override fun getTitle(): String {
        return appPackage.name
    }

}