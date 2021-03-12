package com.theapache64.stackzy.data.repo

import com.akdeniz.googleplaycrawler.GooglePlayAPI
import com.github.theapache64.gpa.api.Play
import com.github.theapache64.gpa.model.Account
import com.malinskiy.adam.request.pkg.Package
import com.theapache64.stackzy.data.local.AndroidApp
import com.theapache64.stackzy.util.bytesToMb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject

class PlayStoreRepo @Inject constructor() {

    suspend fun search(
        keyword: String,
        api: GooglePlayAPI,
        maxSearchResult: Int = 30
    ): List<AndroidApp> {
        return mutableListOf<AndroidApp>()
            .apply {
                var serp = Play.search(query = keyword, api = api)

                // either no more page or loaded 100 or more items
                while (serp.nextPageUrl?.isNotBlank() == true && serp.content.distinctBy { it.docid }.size <= maxSearchResult) {
                    serp = Play.search(query = keyword, api = api, serp)
                }

                serp.content
                    .distinctBy { it.docid }
                    .forEach { item ->

                        val sizeInMb = item.details.appDetails.installDetails.totalApkSize.bytesToMb.let { sizeInMb ->
                            "%.2f".format(Locale.US, sizeInMb).toFloat()
                        }

                        add(
                            AndroidApp(
                                appPackage = Package(item.docid),
                                appTitle = item.title,
                                imageUrl = item.imageList[1].imageUrl,
                                appSize = "$sizeInMb MB"
                            )
                        )
                    }
            }
    }

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