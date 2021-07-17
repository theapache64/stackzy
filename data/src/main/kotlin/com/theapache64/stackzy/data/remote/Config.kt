package com.theapache64.stackzy.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Config(
    @Json(name = "should_consider_result_cache")
    val shouldConsiderResultCache: Boolean,
    @Json(name = "latest_stackzy_lib_version")
    val latestStackzyLibVersion: Int,
    @Json(name = "mandatory_version_code")
    val mandatoryVersionCode: Int,
    @Json(name = "is_browse_by_lib_enabled")
    val isBrowseByLibEnabled: Boolean
)