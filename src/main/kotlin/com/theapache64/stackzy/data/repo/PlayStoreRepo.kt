package com.theapache64.stackzy.data.repo

import com.akdeniz.googleplaycrawler.GooglePlayAPI
import com.github.theapache64.gpa.api.Play
import com.github.theapache64.gpa.model.Account
import com.malinskiy.adam.request.pkg.Package
import com.theapache64.stackzy.data.local.AndroidApp
import java.io.File
import java.io.FileOutputStream
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
                        add(
                            AndroidApp(
                                appPackage = Package(item.docid),
                                appTitle = item.title,
                                imageUrl = item.imageList[1].imageUrl
                            )
                        )
                    }
            }
    }

    fun downloadApk(
        account: Account,
        packageName: String
    ): File {
        val apkFile = kotlin.io.path.createTempFile(packageName, ".apk").toFile()
        val api = Play.getApi(account)
        val appDetails = api.details(packageName)
        val downloadData = api.purchaseAndDeliver(
            packageName,
            appDetails.docV2.details.appDetails.versionCode,
            1
        )
        downloadData.openApp().use { input ->
            FileOutputStream(apkFile).use { output ->
                input.copyTo(output)
            }
        }
        return apkFile
    }

}