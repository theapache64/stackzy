package com.theapache64.stackzy.data.repo

import com.akdeniz.googleplaycrawler.GooglePlayAPI
import com.github.theapache64.gpa.api.Play
import com.github.theapache64.gpa.model.Account
import com.malinskiy.adam.request.pkg.Package
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.data.util.bytesToMb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject

class PlayStoreRepo @Inject constructor() {

    /**
     * To search with the given keyword in play store
     */
    suspend fun search(
        keyword: String,
        api: GooglePlayAPI,
        maxSearchResult: Int = 30
    ): List<AndroidApp> = withContext(Dispatchers.IO) {
        // First search
        var serp = Play.search(query = keyword, api = api)

        // loading more pages until "either no more page" or "maxSearchResult or more items"
        while (
            serp.nextPageUrl?.isNotBlank() == true &&
            serp.content.distinctBy { it.docid }.size <= maxSearchResult
        ) {
            serp = Play.search(query = keyword, api = api, serp)
        }

        // We've loaded all results from network. Now let's sanitize and convert it to AndroidApp class
        serp.content
            .distinctBy { it.docid } // To remove duplicates
            .map { item ->

                // Convert bytes to MB (to readable format)
                val appDetails = item.details.appDetails
                val sizeInMb = appDetails.installDetails.totalApkSize.bytesToMb.let { sizeInMb ->
                    "%.2f".format(Locale.US, sizeInMb).toFloat()
                }

                // Adding app to final list
                AndroidApp(
                    appPackage = Package(item.docid), // Package name
                    appTitle = item.title, // App title
                    versionCode = appDetails.versionCode,
                    versionName = appDetails.versionString,
                    imageUrl = item.imageList[1].imageUrl, // Logo URL
                    appSize = "$sizeInMb MB", // APK Size
                    isSystemApp = false,
                )
            }
    }

    /**
     * To download APK for the given packageName and write to given apkFile using given account
     */
    @Suppress("BlockingMethodInNonBlockingContext")
    fun downloadApk(
        apkFile: File,
        account: Account,
        packageName: String
    ) = flow {
        val api = Play.getApi(account)
        val appDetails = api.details(packageName)
        val downloadData = api.purchaseAndDeliver(
            packageName,
            appDetails.docV2.details.appDetails.versionCode,
            1
        )

        val totalSize = downloadData.appSize

        // Starting download
        downloadData.openApp().use { input ->
            FileOutputStream(apkFile).use { output ->
                val buffer = ByteArray(1024)
                var read: Int
                var counter = 0f
                while (input.read(buffer).also { read = it } != -1) {
                    // Write
                    output.write(buffer, 0, read)

                    // Update progress
                    counter += read
                    val percentage = (counter / totalSize) * 100
                    emit(percentage.toInt())
                }

                // Finish progress
                emit(100)
            }
        }
    }.flowOn(Dispatchers.IO)

}