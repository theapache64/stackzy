package com.theapache64.stackzy.ui.feature.selectapp

import dagger.Component

@Component
interface SelectAppComponent {
    fun inject(selectAppScreenComponent: SelectAppScreenComponent)
}