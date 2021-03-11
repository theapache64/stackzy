package com.theapache64.stackzy.data.repo

import com.akdeniz.googleplaycrawler.GooglePlayAPI
import com.github.theapache64.gpa.api.Play
import com.malinskiy.adam.request.pkg.Package
import com.theapache64.stackzy.data.local.AndroidApp
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

}