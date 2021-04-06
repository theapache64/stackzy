package com.theapache64.stackzy.data.util

object AndroidVersionIdentifier {
    private val androidVersionMap by lazy {
        mutableMapOf(
            3 to "Cupcake",
            4 to "Donut",
            5..7 to "Eclair",
            8 to "Froyo",
            9..10 to "Gingerbread",
            11..13 to "Honeycomb",
            14..15 to "Ice Cream Sandwich",
            16..18 to "Jelly Bean",
            19..20 to "KitKat",
            21..22 to "Lollipop",
            23 to "Marshmallow",
            24..25 to "Nougat",
            26..27 to "Oreo",
            28 to "Pie",
            29 to "Android 10",
            30 to "Android 11",
            31 to "Android 12",
        )
    }

    fun getVersion(sdkInt: Int): String? {
        var versionName: String? = null
        for ((key, value) in androidVersionMap) {

            if (key is Int && key == sdkInt) {
                // Int
                versionName = value
            } else if (key is IntRange && key.contains(sdkInt)) {
                // Int range
                versionName = value
            }

            if (versionName != null) {
                break
            }
        }
        return versionName
    }
}