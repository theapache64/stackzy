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
    private lateinit var jadxRepo: JadxRepo

    @InjectFromComponent
    private lateinit var librariesRepo: LibrariesRepo

    @InjectFromComponent
    private lateinit var apkAnalyzerRepo: ApkAnalyzerRepo


    @Test
    fun `Analysis Report - Native - Cached`() = runBlockingUnitTest {
        librariesRepo.loadLibs { libs ->
            getCachedDecompiledApk(NATIVE_KOTLIN_APK_FILE_NAME) { nativeApkFile, decompiledDir ->
                val report = apkAnalyzerRepo.analyze(NATIVE_KOTLIN_PACKAGE_NAME, nativeApkFile, decompiledDir, libs)
                report.appName.should.equal(NATIVE_KOTLIN_APP_NAME)
                report.packageName.should.equal("com.theapache64.topcorn")
                report.platform.should.instanceof(Platform.NativeKotlin::class.java)
                report.transitiveLibs.size.should.above(0)
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

    private suspend fun getCachedDecompiledApk(
        testResName: String,
        callback: suspend (
            nativeApkFile: File, decompiledDir: File
        ) -> Unit
    ) {
        val decompiledDir = File("build${File.separator}$testResName${File.separator}_decompiled")
        val nativeApkFile = getTestResource(testResName)
        if (decompiledDir.exists().not()) {
            // decompile
            jadxRepo.decompile(nativeApkFile, decompiledDir)
        }

        callback(nativeApkFile, decompiledDir)
    }

    @Test
    fun `Parse permissions`() = runBlockingUnitTest {
        getCachedDecompiledApk(NATIVE_KOTLIN_APK_FILE_NAME) { _, decompiledDir ->
            val permissions = apkAnalyzerRepo.getPermissions(decompiledDir)
            permissions.size.should.above(0)
        }
    }

    @Test
    fun `Parse gradle info`() = runBlockingUnitTest {
        getCachedDecompiledApk(NATIVE_KOTLIN_APK_FILE_NAME) { _, decompiledDir ->
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
    fun `Parse gradle info 2`() = runBlockingUnitTest {
        getCachedDecompiledApk("netflix.apk") { _, decompiledDir ->
            val gradleInfo = apkAnalyzerRepo.getGradleInfo(decompiledDir)
            gradleInfo.versionCode.should.equal(40101)
            gradleInfo.versionName.should.equal("8.11.1 build 12 40101")
            gradleInfo.run {
                minSdk.should.equal(GradleInfo.Sdk(24, "Nougat"))
                targetSdk.should.equal(GradleInfo.Sdk(30, "Android 11"))
            }
        }
    }

    @Test
    fun `Analysis Report - Native`() = runBlockingUnitTest {
        // First, lets decompile a native kotlin apk file
        librariesRepo.loadLibs { libs ->
            Arbor.d("Starting test... ;)")
            getCachedDecompiledApk(NATIVE_KOTLIN_APK_FILE_NAME) { nativeApkFile, decompiledDir ->
                val report = apkAnalyzerRepo.analyze(NATIVE_KOTLIN_PACKAGE_NAME, nativeApkFile, decompiledDir, libs)
                report.appName.should.equal(NATIVE_KOTLIN_APP_NAME)
                report.platform.should.instanceof(Platform.NativeKotlin::class.java)
                report.appLibs.size.should.above(0)
                report.transitiveLibs.size.should.above(0)
                report.appLibs.find {
                    it.name == "ViewModel" ||
                            it.name == "Room"
                }.should.not.`null`
                report.transitiveLibs.filter {
                    it.name == "Glide" ||
                            it.name == "okio" ||
                            it.name == "Calligraphy3"
                }.should.not.`null`
            }
        }
    }

    @Test
    fun `Analysis Report - Flutter`() = runBlockingUnitTest {
        librariesRepo.loadLibs { libs ->
            getCachedDecompiledApk(FLUTTER_APK_FILE_NAME) { apkFile, decompiledDir ->
                val report = apkAnalyzerRepo.analyze(FLUTTER_PACKAGE_NAME, apkFile, decompiledDir, libs)
                report.appName.should.equal(FLUTTER_APP_NAME)
                report.platform.should.instanceof(Platform.Flutter::class.java)
            }

        }
    }

    @Test
    fun `Analysis Report - React Native`() = runBlockingUnitTest {
        librariesRepo.loadLibs {
            getCachedDecompiledApk(REACT_NATIVE_APK_FILE_NAME) { apkFile, decompiledDir ->
                val report = apkAnalyzerRepo.analyze(REACT_NATIVE_APP_NAME, apkFile, decompiledDir, it)
                report.appName.should.equal(REACT_NATIVE_APP_NAME)
                report.platform.should.instanceof(Platform.ReactNative::class.java)
            }
        }
    }

    @Test
    fun `Fetch appName label from manifest`() = runBlockingUnitTest {
        getCachedDecompiledApk(NATIVE_KOTLIN_APK_FILE_NAME) { _, decompiledDir ->
            apkAnalyzerRepo.getAppNameLabel(decompiledDir).should.equal("@string/app_name")
        }
    }

    @Test
    fun `Fetch labelValue from string-xml`() = runBlockingUnitTest {
        getCachedDecompiledApk(NATIVE_KOTLIN_APK_FILE_NAME) { _, decompiledDir ->
            apkAnalyzerRepo.getStringXmlValue(decompiledDir, "@string/app_name").should.equal(NATIVE_KOTLIN_APP_NAME)
        }
    }

    @Test
    fun `Fetch appName`() = runBlockingUnitTest {
        getCachedDecompiledApk(NATIVE_KOTLIN_APK_FILE_NAME) { _, decompiledDir ->
            apkAnalyzerRepo.getAppName(decompiledDir).should.equal(NATIVE_KOTLIN_APP_NAME)
        }
    }

    @Test
    fun `Get platform - kotlin android`() = runBlockingUnitTest {
        getCachedDecompiledApk(NATIVE_KOTLIN_APK_FILE_NAME) { _, decompiledDir ->
            apkAnalyzerRepo.getPlatform(decompiledDir).should.instanceof(Platform.NativeKotlin::class.java)
        }
    }

    @Test
    fun `Get platform - java android`() = runBlockingUnitTest {
        getCachedDecompiledApk(NATIVE_JAVA_APK_FILE_NAME) { _, decompiledDir ->
            apkAnalyzerRepo.getPlatform(decompiledDir).should.instanceof(Platform.NativeJava::class.java)
        }
    }


    @Test
    fun `Get platform - react native`() = runBlockingUnitTest {
        getCachedDecompiledApk(REACT_NATIVE_APK_FILE_NAME) { _, decompiledDir ->
            apkAnalyzerRepo.getPlatform(decompiledDir).should.instanceof(Platform.ReactNative::class.java)
        }
    }

    @Test
    fun `Get platform - flutter`() = runBlockingUnitTest {
        getCachedDecompiledApk(FLUTTER_APK_FILE_NAME) { _, decompiledDir ->
            apkAnalyzerRepo.getPlatform(decompiledDir).should.instanceof(Platform.Flutter::class.java)
        }
    }

    @Test
    fun `Get platform - cordova`() = runBlockingUnitTest {
        getCachedDecompiledApk(CORDOVA_APK_FILE_NAME) { _, decompiledDir ->
            with(apkAnalyzerRepo) {
                getPlatform(decompiledDir).should.instanceof(Platform.Cordova::class.java)
                getAppName(decompiledDir).should.equal(CORDOVA_APP_NAME)
            }
        }
    }

    @Test
    fun `Get platform - unity`() = runBlockingUnitTest {
        getCachedDecompiledApk("com.Dani.Balls_1.18_unity.apk") { _, decompiledDir ->
            with(apkAnalyzerRepo) {
                getPlatform(decompiledDir).should.instanceof(Platform.Unity::class.java)
                getAppName(decompiledDir).should.equal("Balls?")
            }
        }
    }


    @Test
    fun `Get platform - xamarin`() = runBlockingUnitTest {
        getCachedDecompiledApk(XAMARIN_APK_FILE_NAME) { _, decompiledDir ->
            with(apkAnalyzerRepo) {
                getPlatform(decompiledDir).should.instanceof(Platform.Xamarin::class.java)
                getAppName(decompiledDir).should.equal(XAMARIN_APP_NAME)
            }
        }
    }

    @Test
    fun `Get libraries - native kotlin`() = runBlockingUnitTest {
        librariesRepo.loadLibs { libs ->
            getCachedDecompiledApk(NATIVE_KOTLIN_APK_FILE_NAME) { _, decompiledDir ->
                val libResult = apkAnalyzerRepo.getLibResult(decompiledDir, libs)
                libResult.appLibs.size.should.above(0)
                libResult.transitiveDeps.size.should.above(0)
            }
        }
    }

    @Test
    fun `Get categorized libraries - native kotlin`() = runBlockingUnitTest {
        librariesRepo.loadLibs { libs ->
            getCachedDecompiledApk(NATIVE_KOTLIN_APK_FILE_NAME){_, decompiledDir ->
                val libResult = apkAnalyzerRepo.getLibraries(
                    Platform.NativeKotlin(),
                    decompiledDir,
                    libs
                )
                libResult.appLibs.size.should.above(0)
            }
        }
    }

    @Test
    fun `Manifest permission parsing`() {
        val manifestFile = getTestResource("com.netflix.mediaclient_AndroidManifest.xml")
        val permission = apkAnalyzerRepo.getPermissionsFromManifestFile(manifestFile.toPath())

        permission.size.should.above(0)
    }
}