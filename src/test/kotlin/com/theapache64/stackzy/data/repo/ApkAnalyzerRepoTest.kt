package com.theapache64.stackzy.data.repo

import com.github.theapache64.expekt.should
import com.theapache64.stackzy.data.local.GradleInfo
import com.theapache64.stackzy.data.local.Platform
import com.theapache64.stackzy.test.*
import com.theapache64.stackzy.util.loadLibs
import com.toxicbakery.logging.Arbor
import it.cosenonjaviste.daggermock.InjectFromComponent
import org.junit.Rule
import org.junit.Test
import java.io.File


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
    fun `Analysis Report - Native - Cached`() = runBlockingUnitTest {
        librariesRepo.loadLibs { libs ->
            getCachedDecompiledApk { nativeApkFile, decompiledDir ->
                val report = apkAnalyzerRepo.analyze(NATIVE_KOTLIN_PACKAGE_NAME, nativeApkFile, decompiledDir, libs)
                report.appName.should.equal(NATIVE_KOTLIN_APP_NAME)
                report.packageName.should.equal("com.theapache64.topcorn")
                report.platform.should.instanceof(Platform.NativeKotlin::class.java)
                report.libraries.size.should.above(0)
                report.apkSizeInMb.toDouble().should.closeTo(6.5, 0.5)
                report.assetsDir?.exists().should.`true`
                report.permissions.size.should.equal(1) // INTERNET only
                report.gradleInfo.run {
                    minSdk.should.equal(GradleInfo.Sdk(16, "Jelly Bean"))
                    targetSdk.should.equal(GradleInfo.Sdk(29, "Android 10"))
                    versionName.should.equal("1.0.4-alpha03")
                    versionCode.should.equal(10403)
                }

            }
        }
    }

    private suspend fun getCachedDecompiledApk(callback: suspend (nativeApkFile: File, decompiledDir: File) -> Unit) {
        val decompiledDir = File("build${File.separator}topcorn_decompiled")
        val nativeApkFile = getTestResource(NATIVE_KOTLIN_APK_FILE_NAME)
        if (decompiledDir.exists().not()) {
            // decompile
            apkToolRepo.decompile(nativeApkFile, decompiledDir)
        }

        callback(nativeApkFile, decompiledDir)
    }

    @Test
    fun `Parse permissions`() = runBlockingUnitTest {
        getCachedDecompiledApk { _, decompiledDir ->
            val permissions = apkAnalyzerRepo.getPermissions(decompiledDir)
            permissions.size.should.above(0)
        }
    }

    @Test
    fun `Parse gradle info`() = runBlockingUnitTest {
        getCachedDecompiledApk { _, decompiledDir ->
            val gradleInfo = apkAnalyzerRepo.getGradleInfo(decompiledDir)
            gradleInfo.versionCode.should.equal(10403)
            gradleInfo.versionName.should.equal("1.0.4-alpha03")
            gradleInfo.run {
                minSdk.should.equal(GradleInfo.Sdk(16, "Jelly Bean"))
                targetSdk.should.equal(GradleInfo.Sdk(29, "Android 10"))
            }
        }
    }

    @Test
    fun `Analysis Report - Native`() = runBlockingUnitTest {
        // First, lets decompile a native kotlin apk file
        librariesRepo.loadLibs { libs ->
            Arbor.d("Starting test... ;)")
            val nativeApkFile = getTestResource(NATIVE_KOTLIN_APK_FILE_NAME)
            val decompiledDir = apkToolRepo.decompile(nativeApkFile)
            val report = apkAnalyzerRepo.analyze(NATIVE_KOTLIN_PACKAGE_NAME, nativeApkFile, decompiledDir, libs)
            report.appName.should.equal(NATIVE_KOTLIN_APP_NAME)
            report.platform.should.instanceof(Platform.NativeKotlin::class.java)
            report.libraries.size.should.above(0)
        }
    }

    @Test
    fun `Analysis Report - Flutter`() = runBlockingUnitTest {
        librariesRepo.loadLibs { libs ->
            val sampleApkFile = getTestResource(FLUTTER_APK_FILE_NAME)
            val decompiledDir = apkToolRepo.decompile(sampleApkFile)
            val report = apkAnalyzerRepo.analyze(FLUTTER_PACKAGE_NAME, sampleApkFile, decompiledDir, libs)
            report.appName.should.equal(FLUTTER_APP_NAME)
            report.platform.should.instanceof(Platform.Flutter::class.java)
        }
    }

    @Test
    fun `Analysis Report - React Native`() = runBlockingUnitTest {
        librariesRepo.loadLibs {
            val sampleApkFile = getTestResource(REACT_NATIVE_APK_FILE_NAME)
            val decompiledDir = apkToolRepo.decompile(sampleApkFile)
            val report = apkAnalyzerRepo.analyze(REACT_NATIVE_APP_NAME, sampleApkFile, decompiledDir, it)
            report.appName.should.equal(REACT_NATIVE_APP_NAME)
            report.platform.should.instanceof(Platform.ReactNative::class.java)
        }
    }

    @Test
    fun `Fetch appName label from manifest`() = runBlockingUnitTest {
        val paperCopApkFile = getTestResource(NATIVE_KOTLIN_APK_FILE_NAME)
        val decompiledDir = apkToolRepo.decompile(paperCopApkFile)
        apkAnalyzerRepo.getAppNameLabel(decompiledDir).should.equal("@string/app_name")
    }

    @Test
    fun `Fetch labelValue from string-xml`() = runBlockingUnitTest {
        val paperCopApkFile = getTestResource(NATIVE_KOTLIN_APK_FILE_NAME)
        val decompiledDir = apkToolRepo.decompile(paperCopApkFile)
        apkAnalyzerRepo.getStringXmlValue(decompiledDir, "@string/app_name").should.equal(NATIVE_KOTLIN_APP_NAME)
    }

    @Test
    fun `Fetch appName`() = runBlockingUnitTest {
        val paperCopApkFile = getTestResource(NATIVE_KOTLIN_APK_FILE_NAME)
        val decompiledDir = apkToolRepo.decompile(paperCopApkFile)
        apkAnalyzerRepo.getAppName(decompiledDir).should.equal(NATIVE_KOTLIN_APP_NAME)
    }

    @Test
    fun `Get platform - kotlin android`() = runBlockingUnitTest {
        val sampleApkFile = getTestResource(NATIVE_KOTLIN_APK_FILE_NAME)
        val decompiledDir = apkToolRepo.decompile(sampleApkFile)
        apkAnalyzerRepo.getPlatform(decompiledDir).should.instanceof(Platform.NativeKotlin::class.java)
    }

    @Test
    fun `Get platform - java android`() = runBlockingUnitTest {
        val statsBrowserApkFile = getTestResource(NATIVE_JAVA_APK_FILE_NAME)
        statsBrowserApkFile.exists().should.`true`
        val decompiledDir = apkToolRepo.decompile(statsBrowserApkFile)
        apkAnalyzerRepo.getPlatform(decompiledDir).should.instanceof(Platform.NativeJava::class.java)
    }


    @Test
    fun `Get platform - react native`() = runBlockingUnitTest {
        val reactNativeSampleApp = getTestResource(REACT_NATIVE_APK_FILE_NAME)
        reactNativeSampleApp.exists().should.`true`
        val decompiledDir = apkToolRepo.decompile(reactNativeSampleApp)
        apkAnalyzerRepo.getPlatform(decompiledDir).should.instanceof(Platform.ReactNative::class.java)
    }

    @Test
    fun `Get platform - flutter`() = runBlockingUnitTest {
        val flutterAppApk = getTestResource(FLUTTER_APK_FILE_NAME)
        flutterAppApk.exists().should.`true`
        val decompiledDir = apkToolRepo.decompile(flutterAppApk)
        apkAnalyzerRepo.getPlatform(decompiledDir).should.instanceof(Platform.Flutter::class.java)
    }

    @Test
    fun `Get platform - cordova`() = runBlockingUnitTest {
        val sampleApkFile = getTestResource(CORDOVA_APK_FILE_NAME)
        sampleApkFile.exists().should.`true`
        val decompiledDir = apkToolRepo.decompile(sampleApkFile)
        with(apkAnalyzerRepo) {
            getPlatform(decompiledDir).should.instanceof(Platform.Cordova::class.java)
            getAppName(decompiledDir).should.equal(CORDOVA_APP_NAME)
        }
    }

    @Test
    fun `Get platform - unity`() = runBlockingUnitTest {
        val unityApkFile = getTestResource("com.Dani.Balls_1.18_unity.apk")
        unityApkFile.exists().should.`true`
        val decompiledDir = apkToolRepo.decompile(unityApkFile)
        with(apkAnalyzerRepo) {
            getPlatform(decompiledDir).should.instanceof(Platform.Unity::class.java)
            getAppName(decompiledDir).should.equal("Balls?")
        }
    }


    @Test
    fun `Get platform - xamarin`() = runBlockingUnitTest {
        val sampleApkFile = getTestResource(XAMARIN_APK_FILE_NAME)
        sampleApkFile.exists().should.`true`
        val decompiledDir = apkToolRepo.decompile(sampleApkFile)
        with(apkAnalyzerRepo) {
            getPlatform(decompiledDir).should.instanceof(Platform.Xamarin::class.java)
            getAppName(decompiledDir).should.equal(XAMARIN_APP_NAME)
        }
    }

    @Test
    fun `Get libraries - native kotlin`() = runBlockingUnitTest {
        librariesRepo.loadLibs { libs ->
            val sampleApkFile = getTestResource(NATIVE_KOTLIN_APK_FILE_NAME)
            val decompiledDir = apkToolRepo.decompile(sampleApkFile)
            val (appLibraries, untrackedLibs) = apkAnalyzerRepo.getAppLibraries(decompiledDir, libs)
            appLibraries.size.should.above(0)
            untrackedLibs.size.should.above(0)
        }
    }

    @Test
    fun `Get categorized libraries - native kotlin`() = runBlockingUnitTest {
        librariesRepo.loadLibs { libs ->
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

    @Test
    fun `Manifest permission parsing`() {
        val manifestFile = getTestResource("com.netflix.mediaclient_AndroidManifest.xml")
        val permission = apkAnalyzerRepo.getPermissionsFromManifestFile(manifestFile.toPath())

        permission.size.should.above(0)
    }
}