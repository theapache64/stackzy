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
        val topCornApkFile = getTestResource("com.theapache64.papercop_kotlin_android.apk")
        val decompiledDir = apkToolRepo.decompile(topCornApkFile)
        val report = apkAnalyzerRepo.analyze(decompiledDir)
        report.appName.should.equal("TopCorn")
        report.platform.should.instanceof(Platform.NativeKotlin::class.java)
        report.libraries.size.should.above(0)
    }

    @Test
    fun `Fetch appName label from manifest`() {
        val topCornApkFile = getTestResource("com.theapache64.papercop_kotlin_android.apk")
        val decompiledDir = apkToolRepo.decompile(topCornApkFile)
        apkAnalyzerRepo.getAppNameLabel(decompiledDir).should.equal("app_name")
    }

    @Test
    fun `Fetch labelValue from string-xml`() {
        val topCornApkFile = getTestResource("com.theapache64.papercop_kotlin_android.apk")
        val decompiledDir = apkToolRepo.decompile(topCornApkFile)
        apkAnalyzerRepo.getStringXmlValue(decompiledDir, "app_name").should.equal("Paper Cop")
    }

    @Test
    fun `Fetch appName`() {
        val topCornApkFile = getTestResource("com.theapache64.papercop_kotlin_android.apk")
        val decompiledDir = apkToolRepo.decompile(topCornApkFile)
        apkAnalyzerRepo.getAppName(decompiledDir).should.equal("Paper Cop")
    }
}