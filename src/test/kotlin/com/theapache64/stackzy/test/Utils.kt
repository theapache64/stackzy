package com.theapache64.stackzy.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import java.io.File
import java.net.URL

const val FLUTTER_APP_NAME = "Flutter Tutorial"
const val FLUTTER_PACKAGE_NAME = "com.sts.flutter"
const val FLUTTER_APK_FILE_NAME = "com.sts.flutter_flutter.apk"

const val NATIVE_KOTLIN_APP_NAME = "TopCorn"
const val NATIVE_KOTLIN_PACKAGE_NAME = "com.theapache64.topcorn"
const val NATIVE_KOTLIN_APK_FILE_NAME = "com.theapache64.topcorn_kotlin_android.apk"

const val CORDOVA_APP_NAME = "FinC Financial Calculators"
const val CORDOVA_PACKAGE_NAME = "com.swot.emicalculator"
const val CORDOVA_APK_FILE_NAME = "com.swot.emicalculator_cordova.apk"

const val XAMARIN_APP_NAME = "Xamarin Samples"
const val XAMARIN_PACKAGE_NAME = "com.mobmaxime.xamarin"
const val XAMARIN_APK_FILE_NAME = "com.mobmaxime.xamarin_xamarin.apk"

const val NATIVE_JAVA_APK_FILE_NAME = "com.theah64.whatsappstatusbrowser_java_android.apk"

const val REACT_NATIVE_APP_NAME = "React Native Animation Examples"
const val REACT_NATIVE_PACKAGE_NAME = "com.reactnativeanimationexamples"
const val REACT_NATIVE_APK_FILE_NAME = "com.reactnativeanimationexamples_react_native.apk"

fun runBlockingUnitTest(block: suspend (scope: CoroutineScope) -> Unit) = runBlocking {
    block(this)
}

fun getTestResource(name: String): File {
    val url: URL = Thread.currentThread().contextClassLoader.getResource(name)!!
    return File(url.path)
}