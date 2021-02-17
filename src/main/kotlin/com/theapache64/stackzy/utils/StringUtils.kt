package com.theapache64.stackzy.utils

object StringUtils {
    private val apostropheRegEx by lazy {
        "^\".*[']+.*\"\$".toRegex()
    }

    fun removeApostrophe(input: String): String {
        return if (apostropheRegEx.matches(input)) {
            input.substring(1, input.length - 1)
        } else {
            input
        }
    }
}