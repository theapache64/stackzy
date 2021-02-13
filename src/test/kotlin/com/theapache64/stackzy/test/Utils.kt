package com.theapache64.stackzy.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import java.io.File
import java.net.URL

const val FLUTTER_APP_NAME = "Flutter Tutorial"
const val FLUTTER_PACKAGE_NAME = "com.sts.flutter"
const val FLUTTER_APK_FILE_NAME = "com.sts.flutter_flutter.apk"

const val NATIVE_KOTLIN_APP_NAME = "Paper Cop"
const val NATIVE_KOTLIN_PACKAGE_NAME = "com.theapache64.topcorn"
const val NATIVE_KOTLIN_APK_FILE_NAME = "com.theapache64.papercop_kotlin_android.apk"

const val NATIVE_JAVA_APK_FILE_NAME = "com.theah64.whatsappstatusbrowser_java_android.apk"

const val REACT_NATIVE_APK_FILE_NAME = "com.reactnativeanimationexamples_react_native.apk"

fun runBlockingUnitTest(block: suspend (scope: CoroutineScope) -> Unit) = runBlocking {
    block(this)
    Unit
}

fun getTestResource(name: String): File {
    val url: URL = Thread.currentThread().contextClassLoader.getResource(name)!!
    return File(url.path)
}