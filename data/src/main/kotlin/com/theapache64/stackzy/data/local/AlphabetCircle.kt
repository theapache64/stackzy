package com.theapache64.stackzy.data.local

abstract class BaseAlphabetCircle {

    abstract fun getTitle(): String
    abstract fun getSubtitle(): String
    open fun getSubtitle2(): String? = null
    abstract fun imageUrl(): String?
    open fun getAlphabet() = getTitle().first()
}