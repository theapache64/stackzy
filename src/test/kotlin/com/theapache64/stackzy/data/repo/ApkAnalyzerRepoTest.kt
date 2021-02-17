package com.theapache64.stackzy.data.repo

import com.theapache64.expekt.should
import com.theapache64.stackzy.data.local.Platform
import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.test.*
import com.theapache64.stackzy.utils.calladapter.flow.Resource
import it.cosenonjaviste.daggermock.InjectFromComponent
import kotlinx.coroutines.flow.collect
import org.junit.Rule
import org.junit.Test


class ApkAnalyzerRepoTest {

    @get:Rule
    val daggerMockRule = MyDaggerMockRule()

    @InjectFromComponent
    private lateinit var apkToolRepo: ApkToolRepo

    @InjectFromComponent
    private lateinit var librariesRepo: LibrariesRepo

    @InjectFromComponent
    private lateinit var apkAnalyzerRepo: ApkAnalyzerRepo


    @Test
    fun `Analysis Report - Native`() = runBlockingUnitTest {
        // First, lets decompile a native kotlin apk file
        loadLibs { libs ->
            val paperCopApkFile = getTestResource(NATIVE_KOTLIN_APK_FILE_NAME)
            val decompiledDir = apkToolRepo.decompile(paperCopApkFile)
            val report = apkAnalyzerRepo.analyze(NATIVE_KOTLIN_PACKAGE_NAME, decompiledDir, libs)
            report.appName.should.equal(NATIVE_KOTLIN_APP_NAME)
            report.platform.should.instanceof(Platform.NativeKotlin::class.java)
            report.libraries.size.should.above(0)
        }
    }


    @Test
    fun `Analysis Report - Flutter`() = runBlockingUnitTest {
        loadLibs { libs ->
            val sampleApkFile = getTestResource(FLUTTER_APK_FILE_NAME)
            val decompiledDir = apkToolRepo.decompile(sampleApkFile)
            val report = apkAnalyzerRepo.analyze(FLUTTER_PACKAGE_NAME,decompiledDir, libs)
            report.appName.should.equal(FLUTTER_APP_NAME)
            report.platform.should.instanceof(Platform.Flutter::class.java)
        }
    }

    @Test
    fun `Analysis Report - React Native`() = runBlockingUnitTest {
        loadLibs {
            val sampleApkFile = getTestResource(REACT_NATIVE_APK_FILE_NAME)
            val decompiledDir = apkToolRepo.decompile(sampleApkFile)
            val report = apkAnalyzerRepo.analyze(REACT_NATIVE_APP_NAME, decompiledDir, it)
            report.appName.should.equal(REACT_NATIVE_APP_NAME)
            report.platform.should.instanceof(Platform.ReactNative::class.java)
        }
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
        val sampleApkFile = getTestResource(NATIVE_KOTLIN_APK_FILE_NAME)
        val decompiledDir = apkToolRepo.decompile(sampleApkFile)
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

    @Test
    fun `Get libraries - native kotlin`() = runBlockingUnitTest {
        loadLibs { libs ->
            val sampleApkFile = getTestResource(NATIVE_KOTLIN_APK_FILE_NAME)
            val decompiledDir = apkToolRepo.decompile(sampleApkFile)
            val (appLibraries, untrackedLibs) = apkAnalyzerRepo.getAppLibraries(decompiledDir, libs)
            appLibraries.size.should.above(0)
            untrackedLibs.size.should.above(0)
        }
    }

    @Test
    fun `Get categorized libraries - native kotlin`() = runBlockingUnitTest {
        loadLibs { libs ->
            val sampleApkFile = getTestResource(NATIVE_KOTLIN_APK_FILE_NAME)
            val decompiledDir = apkToolRepo.decompile(sampleApkFile)
            val (untrackedLibs, appLibraries) = apkAnalyzerRepo.getLibraries(
                Platform.NativeKotlin(),
                decompiledDir,
                libs
            )
            appLibraries.size.should.above(0)
            untrackedLibs.size.should.above(0)
        }
    }

    private suspend fun loadLibs(onLibsLoaded: (List<Library>) -> Unit) {
        librariesRepo.getRemoteLibraries().collect {
            when (it) {
                is Resource.Loading -> {
                    println("Loading libs")
                }
                is Resource.Success -> {
                    onLibsLoaded(it.data)
                }
                is Resource.Error -> {
                    throw IllegalArgumentException(it.errorData)
                }
            }
        }
    }
}