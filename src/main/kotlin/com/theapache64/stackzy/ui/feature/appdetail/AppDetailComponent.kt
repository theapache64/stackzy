package com.theapache64.stackzy.ui.feature.appdetail

import com.theapache64.stackzy.data.local.AndroidApp
import dagger.BindsInstance
import dagger.Component

@Component
interface AppDetailComponent {
    fun inject(appDetailScreenComponent: AppDetailScreenComponent)

    @Component.Builder
    interface Builder {
        fun build(): AppDetailComponent

        @BindsInstance
        fun androidApp(androidApp: AndroidApp): Builder
    }
}