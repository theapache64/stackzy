package com.theapache64.stackzy.data.local

sealed class Platform(val name: String) {
    class NativeKotlin() : Platform("Native Android with Kotlin")
    class NativeJava() : Platform("Native Android with Java")
    class ReactNative() : Platform("React Native")
    class Flutter() : Platform("Flutter")
    class Cordova() : Platform("Apache Cordova")
    class PhoneGap() : Platform("Adobe PhoneGap")
    class Xamarin() : Platform("Xamarin")
}