package com.theapache64.stackzy.ui.feature.pathway

import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.repo.AuthRepo
import com.theapache64.stackzy.data.repo.ConfigRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


class PathwayViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    configRepo: ConfigRepo
) {

    private val config = configRepo.getLocalConfig()

    companion object {
        private const val INFO_MADE_WITH_LOVE = "Made with ❤️"
    }

    private val _loggedInAccount = MutableStateFlow<Account?>(null)
    val loggedInAccount: StateFlow<Account?> = _loggedInAccount

    private val _focusedCardInfo = MutableStateFlow(INFO_MADE_WITH_LOVE)
    val focusedCardInfo = _focusedCardInfo.asStateFlow()

    private val _isBrowseByLibEnabled = MutableStateFlow(config?.isBrowseByLibEnabled ?: false)
    val isBrowseByLibEnabled = _isBrowseByLibEnabled.asStateFlow()

    private val _isPlayStoreEnabled = MutableStateFlow(config?.isPlayStoreEnabled ?: false)
    val isPlayStoreEnabled = _isPlayStoreEnabled.asStateFlow()

    private lateinit var onPlayStoreSelected: () -> Unit
    private lateinit var onLogInNeeded: () -> Unit

    fun init(
        onPlayStoreSelected: () -> Unit,
        onLogInNeeded: () -> Unit,
    ) {
        this.onPlayStoreSelected = onPlayStoreSelected
        this.onLogInNeeded = onLogInNeeded
    }

    fun refreshAccount() {
        val isLoggedIn = authRepo.isLoggedIn()
        _loggedInAccount.value = if (isLoggedIn) {
            authRepo.getAccount()
        } else {
            null
        }
    }

    fun onPlayStoreClicked() {
        // Check if user is logged in
        if (loggedInAccount.value == null) {
            // not logged in
            onLogInNeeded.invoke()
        } else {
            // logged in
            onPlayStoreSelected.invoke()
        }
    }

    fun onLogoutClicked() {
        authRepo.setLoggedIn(false)
        // If 'RememberMe` is disabled, clear account info as well
        if (!authRepo.isRemember()) {
            authRepo.clearAccount()
        }
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
        _focusedCardInfo.value = INFO_MADE_WITH_LOVE
    }

}