package com.theapache64.stackzy.ui.feature.pathway

import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.repo.AuthRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class PathwayViewModel @Inject constructor(
    private val authRepo: AuthRepo
) {

    private val _loggedInAccount = MutableStateFlow<Account?>(null)
    val loggedInAccount: StateFlow<Account?> = _loggedInAccount

    private val _focusedCardInfo = MutableStateFlow("")
    val focusedCardInfo = _focusedCardInfo.asStateFlow()

    private lateinit var onPlayStoreSelected: (Account) -> Unit
    private lateinit var onLogInNeeded: () -> Unit

    fun init(
        onPlayStoreSelected: (Account) -> Unit,
        onLogInNeeded: () -> Unit,
    ) {
        this.onPlayStoreSelected = onPlayStoreSelected
        this.onLogInNeeded = onLogInNeeded
    }

    fun refreshAccount() {
        _loggedInAccount.value = authRepo.getAccount()
    }

    fun onPlayStoreClicked() {
        // Check if user is logged in
        if (loggedInAccount.value == null) {
            // not logged in
            onLogInNeeded.invoke()
        } else {
            // logged in
            onPlayStoreSelected.invoke(loggedInAccount.value!!)
        }
    }

    fun onLogoutClicked() {
        authRepo.logout()
        _loggedInAccount.value = null
    }

    fun onPlayStoreCardFocused() {
        _focusedCardInfo.value = "Browse though PlayStore apps"
    }

    fun onAdbCardFocused() {
        _focusedCardInfo.value = "Browse through connected android device"
    }

    fun onLibrariesCardFocused() {
        _focusedCardInfo.value = "Find apps that are using a specific library"
    }

    fun onCardFocusLost() {
        _focusedCardInfo.value = ""
    }

}