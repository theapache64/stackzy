package com.theapache64.stackzy.util

import java.util.*

/**
 * Types of Operating Systems
 */
enum class OSType {
    Windows, MacOS, Linux, Other
}

object OsCheck {

    /**
     * detect the operating system from the os.name System property a
     *
     * @returns - the operating system detected
     */
    val operatingSystemType: OSType by lazy {

        val os = System
            .getProperty("os.name", "generic")
            .lowercase(Locale.ENGLISH)

        if (os.indexOf("mac") >= 0 || os.indexOf("darwin") >= 0) {
            OSType.MacOS
        } else if (os.indexOf("win") >= 0) {
            OSType.Windows
        } else if (os.indexOf("nux") >= 0) {
            OSType.Linux
        } else {
            OSType.Other
        }
    }


}