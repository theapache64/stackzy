package com.theapache64.stackzy.ui.feature.applist

import com.github.theapache64.expekt.should
import org.junit.Test

class AppListViewModelTest {
    @Test
    fun validUrl() {
        AppListViewModel.isPlayStoreUrl(
            "https://play.google.com/store/apps/details?id=com.theapache64.papercop"
        ).should.`true`
    }

    @Test
    fun invalidUrl() {
        AppListViewModel.isPlayStoreUrl(
            "https://play.google.com/store/apps/details?id=co-m.theapache64.papercop"
        ).should.`false`
    }

    @Test
    fun parseValidPackageName() {
        AppListViewModel.parsePackageName(
            "https://play.google.com/store/apps/details?id=com.theapache64.papercop"
        ).should.equal("com.theapache64.papercop")
    }

    @Test
    fun parseInvalidPackageName() {
        AppListViewModel.parsePackageName(
            "https://play.google.com/store/apps/details?id=345-453"
        ).should.`null`
    }
}