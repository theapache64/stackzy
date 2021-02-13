package com.theapache64.stackzy.data.repo

import com.theapache64.expekt.should
import com.theapache64.stackzy.data.local.Platform
import com.theapache64.stackzy.test.*
import it.cosenonjaviste.daggermock.InjectFromComponent
import org.junit.Rule
import org.junit.Test


class ApkAnalyzerRepoTest {

    @get:Rule
    val daggerMockRule = MyDaggerMockRule()

    @InjectFromComponent
    private lateinit var apkToolRepo: ApkToolRepo

    @InjectFromComponent
    private lateinit var apkAnalyzerRepo: ApkAnalyzerRepo

    @Test
    fun `Analysis Report`() {
        // First, lets decompile a native kotlin apk file
        val paperCopApkFile = getTestResource(NATIVE_KOTLIN_APK_FILE_NAME)
        val decompiledDir = apkToolRepo.decompile(paperCopApkFile)
        val report = apkAnalyzerRepo.analyze(decompiledDir)
        report.appName.should.equal(NATIVE_KOTLIN_APP_NAME)
        report.platform.should.instanceof(Platform.NativeKotlin::class.java)
        // report.libraries.size.should.above(0)
    }


    @Test
    fun `Analysis Report - Flutter`() {
        val sampleApkFile = getTestResource(FLUTTER_APK_FILE_NAME)
        val decompiledDir = apkToolRepo.decompile(sampleApkFile)
        val report = apkAnalyzerRepo.analyze(decompiledDir)
        report.appName.should.equal(FLUTTER_APP_NAME)
        report.platform.should.instanceof(Platform.Flutter::class.java)
    }

    @Test
    fun `Analysis Report - React Native`() {
        val sampleApkFile = getTestResource(REACT_NATIVE_APK_FILE_NAME)
        val decompiledDir = apkToolRepo.decompile(sampleApkFile)
        val report = apkAnalyzerRepo.analyze(decompiledDir)
        report.appName.should.equal(REACT_NATIVE_APP_NAME)
        report.platform.should.instanceof(Platform.ReactNative::class.java)
    }

    @Test
    fun `Fetch appName label from manifest`() {
        val paperCopApkFile = getTestResource(NATIVE_KOTLIN_APK_FILE_NAME)
        val decompiledDir = apkToolRepo.decompile(paperCopApkFile)
        apkAnalyzerRepo.getAppNameLabel(decompiledDir).should.equal("@string/app_name")
    }

    @Test
    fun `Fetch labelValue from string-xml`() {
        val paperCopApkFile = getTestResource(NATIVE_KOTLIN_APK_FILE_NAME)
        val decompiledDir = apkToolRepo.decompile(paperCopApkFile)
        apkAnalyzerRepo.getStringXmlValue(decompiledDir, "@string/app_name").should.equal(NATIVE_KOTLIN_APP_NAME)
    }

    @Test
    fun `Fetch appName`() {
        val paperCopApkFile = getTestResource(NATIVE_KOTLIN_APK_FILE_NAME)
        val decompiledDir = apkToolRepo.decompile(paperCopApkFile)
        apkAnalyzerRepo.getAppName(decompiledDir).should.equal(NATIVE_KOTLIN_APP_NAME)
    }

    @Test
    fun `Get platform - kotlin android`() {
        val paperCopApkFile = getTestResource(NATIVE_KOTLIN_APK_FILE_NAME)
        val decompiledDir = apkToolRepo.decompile(paperCopApkFile)
        apkAnalyzerRepo.getPlatform(decompiledDir).should.instanceof(Platform.NativeKotlin::class.java)
    }

    @Test
    fun `Get platform - java android`() {
        val statsBrowserApkFile = getTestResource(NATIVE_JAVA_APK_FILE_NAME)
        statsBrowserApkFile.exists().should.`true`
        val decompiledDir = apkToolRepo.decompile(statsBrowserApkFile)
        apkAnalyzerRepo.getPlatform(decompiledDir).should.instanceof(Platform.NativeJava::class.java)
    }


    @Test
    fun `Get platform - react native`() {
        val reactNativeSampleApp = getTestResource(REACT_NATIVE_APK_FILE_NAME)
        reactNativeSampleApp.exists().should.`true`
        val decompiledDir = apkToolRepo.decompile(reactNativeSampleApp)
        apkAnalyzerRepo.getPlatform(decompiledDir).should.instanceof(Platform.ReactNative::class.java)
    }

    @Test
    fun `Get platform - flutter`() {
        val flutterAppApk = getTestResource(FLUTTER_APK_FILE_NAME)
        flutterAppApk.exists().should.`true`
        val decompiledDir = apkToolRepo.decompile(flutterAppApk)
        apkAnalyzerRepo.getPlatform(decompiledDir).should.instanceof(Platform.Flutter::class.java)
    }

    @Test
    fun `Get platform - cordova`() {
        val sampleApkFile = getTestResource(CORDOVA_APK_FILE_NAME)
        sampleApkFile.exists().should.`true`
        val decompiledDir = apkToolRepo.decompile(sampleApkFile)
        apkAnalyzerRepo.getPlatform(decompiledDir).should.instanceof(Platform.Cordova::class.java)
    }

    @Test
    fun `Get platform - xamarin`() {
        val sampleApkFile = getTestResource(XAMARIN_APK_FILE_NAME)
        sampleApkFile.exists().should.`true`
        val decompiledDir = apkToolRepo.decompile(sampleApkFile)
        apkAnalyzerRepo.getPlatform(decompiledDir).should.instanceof(Platform.Xamarin::class.java)
    }
}