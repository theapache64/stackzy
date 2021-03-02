package brut.androlib.meta

data class MetaInfo(
    var sdkInfo: SdkInfo? = null,
    var versionInfo: VersionInfo? = null
) {
    data class SdkInfo(
        var minSdkVersion: Int? = null,
        var targetSdkVersion: Int? = null
    )

    data class VersionInfo(
        var versionCode: String? = null,
        var versionName: String? = null
    )
}