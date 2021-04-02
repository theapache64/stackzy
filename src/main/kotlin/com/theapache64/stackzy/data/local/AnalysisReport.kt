package com.theapache64.stackzy.data.local

import com.theapache64.stackzy.data.remote.Config
import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.data.remote.Result
import com.theapache64.stackzy.data.repo.ResultRepo
import java.io.File


data class AnalysisReport(
    val appName: String?,
    val packageName: String,
    val platform: Platform,
    val libraries: List<Library>,
    val untrackedLibraries: Set<String>,
    val apkSizeInMb: Float,
    val assetsDir: File?,
    val permissions: List<String>,
    val gradleInfo: GradleInfo
)


fun AnalysisReport.toResult(resultRepo: ResultRepo, config: Config? = null): Result {
    println("Permissions : $permissions")
    return Result(
        appName = this.appName ?: this.packageName,
        packageName = this.packageName,
        libPackages = this.libraries.joinToString(",") { it.packageName },
        versionName = this.gradleInfo.versionName ?: "Unknown",
        versionCode = this.gradleInfo.versionCode ?: -1,
        platform = this.platform::class.simpleName!!,
        apkSizeInMb = this.apkSizeInMb,
        permissions = this.permissions.joinToString(","),
        gradleInfoJson = resultRepo.jsonify(this.gradleInfo),
        stackzyLibVersion = config?.latestStackzyLibVersion ?: 0,
    )
}