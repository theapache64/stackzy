package com.theapache64.stackzy.data.local

import com.theapache64.stackzy.data.remote.Config
import com.theapache64.stackzy.data.remote.Library
import com.theapache64.stackzy.data.remote.Result
import com.theapache64.stackzy.data.repo.ResultRepo
import java.io.File


interface AnalysisReportDefinition {
    val appName: String?
    val packageName: String
    val platform: Platform
    val libraries: List<Library>
    val untrackedLibraries: Set<String>
    val apkSizeInMb: Float
    val assetsDir: File?
    val permissions: List<String>
    val gradleInfo: GradleInfo
}

class AnalysisReport(
    override val appName: String?,
    override val packageName: String,
    override val platform: Platform,
    override val libraries: List<Library>,
    override val untrackedLibraries: Set<String>,
    override val apkSizeInMb: Float,
    override val assetsDir: File?,
    override val permissions: List<String>,
    override val gradleInfo: GradleInfo
) : AnalysisReportDefinition


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
