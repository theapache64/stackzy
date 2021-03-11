package com.theapache64.stackzy.data.repo

import com.akdeniz.googleplaycrawler.GooglePlayAPI
import com.github.theapache64.gpa.api.Play
import com.malinskiy.adam.request.pkg.Package
import com.theapache64.stackzy.data.local.AndroidApp
import javax.inject.Inject

class PlayStoreRepo @Inject constructor() {

    suspend fun search(keyword: String, api: GooglePlayAPI): List<AndroidApp> {
        return mutableListOf<AndroidApp>()
            .apply {
                val serp = Play.search(query = keyword, api = api).apply {
                    // search again for more result
                    Play.search(keyword, api, this)
                }
                serp.content.forEach { item ->
                    println(item.docid)
                    add(
                        AndroidApp(
                            appPackage = Package(item.docid),
                            appTitle = item.title
                        )
                    )
                }
            }
    }

}