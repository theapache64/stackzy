package com.theapache64.stackzy.ui.feature.pathway

import com.theapache64.stackzy.data.repo.AuthRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class PathwayViewModel @Inject constructor(
    private val authRepo: AuthRepo
) {

    private val _isLogInNeeded = MutableStateFlow(false)
    val isLogInNeeded: StateFlow<Boolean> = _isLogInNeeded

    private val _shouldLaunchStoreSearch = MutableStateFlow(false)
    val shouldLaunchStoreSearch: StateFlow<Boolean> = _shouldLaunchStoreSearch

    fun onPlayStoreClicked() {
        // Check if user is logged in
        val account = authRepo.getAccount()
        if (account == null) {
            // not logged in
            _isLogInNeeded.value = true
        } else {
            // logged in
            _shouldLaunchStoreSearch.value = true
        }
    }

}