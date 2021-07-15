package com.theapache64.stackzy.ui.feature.libdetail

import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class LibraryDetailViewModel @Inject constructor() {
    private lateinit var viewModelScope: CoroutineScope

    fun init(viewModelScope: CoroutineScope) {
        this.viewModelScope = viewModelScope
    }

}