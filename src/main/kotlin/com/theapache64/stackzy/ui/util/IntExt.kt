package com.theapache64.stackzy.ui.util


fun Int.getSingularOrPlural(singular: String, plural: String): String {
    return when {
        this == 1 -> {
            singular
        }

        else -> {
            plural
        }
    }
}
