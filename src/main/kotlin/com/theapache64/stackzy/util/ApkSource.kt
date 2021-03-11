package com.theapache64.stackzy.util

sealed class ApkSource<out A, out B> {
    class Adb<A>(val value: A) : ApkSource<A, Nothing>()
    class PlayStore<B>(val value: B) : ApkSource<Nothing, B>()
}