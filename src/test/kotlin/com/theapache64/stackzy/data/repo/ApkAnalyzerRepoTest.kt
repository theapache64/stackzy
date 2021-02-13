package com.theapache64.stackzy.data.repo

import com.theapache64.expekt.should
import com.theapache64.stackzy.data.local.Platform
import com.theapache64.stackzy.test.MyDaggerMockRule
import com.theapache64.stackzy.test.getTestResource
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
        val paperCopApkFile = getTestResource("com.theapache64.papercop_kotlin_android.apk")
        val decompiledDir = apkToolRepo.decompile(paperCopApkFile)
        val report = apkAnalyzerRepo.analyze(decompiledDir)
        report.appName.should.equal("TopCorn")
        report.platform.should.instanceof(Platform.NativeKotlin::class.java)
        report.libraries.size.should.above(0)
    }

    @Test
    fun `Fetch appName label from manifest`() {
        val paperCopApkFile = getTestResource("com.theapache64.papercop_kotlin_android.apk")
        val decompiledDir = apkToolRepo.decompile(paperCopApkFile)
        apkAnalyzerRepo.getAppNameLabel(decompiledDir).should.equal("app_name")
    }

    @Test
    fun `Fetch labelValue from string-xml`() {
        val paperCopApkFile = getTestResource("com.theapache64.papercop_kotlin_android.apk")
        val decompiledDir = apkToolRepo.decompile(paperCopApkFile)
        apkAnalyzerRepo.getStringXmlValue(decompiledDir, "app_name").should.equal("Paper Cop")
    }

    @Test
    fun `Fetch appName`() {
        val paperCopApkFile = getTestResource("com.theapache64.papercop_kotlin_android.apk")
        val decompiledDir = apkToolRepo.decompile(paperCopApkFile)
        apkAnalyzerRepo.getAppName(decompiledDir).should.equal("Paper Cop")
    }

    @Test
    fun `Get platform - kotlin android`() {
        val paperCopApkFile = getTestResource("com.theapache64.papercop_kotlin_android.apk")
        val decompiledDir = apkToolRepo.decompile(paperCopApkFile)
        apkAnalyzerRepo.getPlatform(decompiledDir).should.instanceof(Platform.NativeKotlin::class.java)
    }

    @Test
    fun `Get platform - java android`() {
        val statsBrowserApkFile = getTestResource("com.theah64.whatsappstatusbrowser_java_android.apk")
        statsBrowserApkFile.exists().should.`true`
        val decompiledDir = apkToolRepo.decompile(statsBrowserApkFile)
        apkAnalyzerRepo.getPlatform(decompiledDir).should.instanceof(Platform.NativeJava::class.java)
    }


    @Test
    fun `Get platform - react native`() {
        val reactNativeSampleApp = getTestResource("com.reactnativeanimationexamples_react_native.apk")
        reactNativeSampleApp.exists().should.`true`
        val decompiledDir = apkToolRepo.decompile(reactNativeSampleApp)
        apkAnalyzerRepo.getPlatform(decompiledDir).should.instanceof(Platform.ReactNative::class.java)
    }

    @Test
    fun `Get platform - flutter`() {
        val flutterAppApk = getTestResource("com.sts.flutter_flutter.apk")
        flutterAppApk.exists().should.`true`
        val decompiledDir = apkToolRepo.decompile(flutterAppApk)
        apkAnalyzerRepo.getPlatform(decompiledDir).should.instanceof(Platform.Flutter::class.java)
    }
}