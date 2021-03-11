package com.theapache64.stackzy.ui.feature.pathway

import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.repo.AuthRepo
import javax.inject.Inject

class PathwayViewModel @Inject constructor(
    private val authRepo: AuthRepo
) {

    private lateinit var onPlayStoreSelected: (Account) -> Unit
    private lateinit var onLogInNeeded: () -> Unit

    fun init(
        onPlayStoreSelected: (Account) -> Unit,
        onLogInNeeded: () -> Unit,
    ) {
        this.onPlayStoreSelected = onPlayStoreSelected
        this.onLogInNeeded = onLogInNeeded
    }

    fun onPlayStoreClicked() {
        // Check if user is logged in
        val account = authRepo.getAccount()
        if (account == null) {
            // not logged in
            onLogInNeeded.invoke()
        } else {
            // logged in
            onPlayStoreSelected.invoke(account)
        }
    }

}