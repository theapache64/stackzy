package com.theapache64.stackzy.data.local

sealed class Platform(val name: String) {
    companion object {
        fun fromClassName(platformClassName: String): Platform {
            return when (platformClassName) {
                NativeKotlin::class.simpleName -> NativeKotlin()
                NativeJava::class.simpleName -> NativeJava()
                ReactNative::class.simpleName -> ReactNative()
                Flutter::class.simpleName -> Flutter()
                Cordova::class.simpleName -> Cordova()
                PhoneGap::class.simpleName -> PhoneGap()
                Xamarin::class.simpleName -> Xamarin()
                else -> throw IllegalArgumentException("Undefined platform '$platformClassName'")
            }
        }
    }

    class NativeKotlin : Platform("Native Android with Kotlin")
    class NativeJava : Platform("Native Android with Java")
    class ReactNative : Platform("React Native")
    class Flutter : Platform("Flutter")
    class Cordova : Platform("Apache Cordova")
    class PhoneGap : Platform("Adobe PhoneGap")
    class Xamarin : Platform("Xamarin")
}