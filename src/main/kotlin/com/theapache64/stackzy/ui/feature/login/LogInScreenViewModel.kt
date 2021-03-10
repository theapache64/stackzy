package com.theapache64.stackzy.ui.feature.login

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class LogInScreenViewModel @Inject constructor() {

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isUsernameError = MutableStateFlow(false)
    val isUsernameError: StateFlow<Boolean> = _isUsernameError

    private val _isPasswordError = MutableStateFlow(false)
    val isPasswordError: StateFlow<Boolean> = _isPasswordError

    private var isSubmitted = false

    fun onNewUsername(newUsername: String) {
        _username.value = newUsername

        // Show error only if the form is submitted
        _isUsernameError.value = isSubmitted && isValidUsername(newUsername)
    }


    fun onNewPassword(newPassword: String) {
        _password.value = newPassword

        // Show error only if the form is submitted
        _isPasswordError.value = isSubmitted && isValidPassword(newPassword)
    }

    fun onLogInClicked() {
        isSubmitted = true

        val username = username.value.trim()
        val password = password.value.trim()

        _isUsernameError.value = isValidUsername(username)
        _isPasswordError.value = isValidPassword(password)
    }

    private fun isValidUsername(newUsername: String) = newUsername.isEmpty()
    private fun isValidPassword(newPassword: String) = newPassword.isEmpty()
}
