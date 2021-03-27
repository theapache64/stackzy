package com.theapache64.stackzy.ui.feature.login

import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.repo.AuthRepo
import com.theapache64.stackzy.util.calladapter.flow.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class LogInScreenViewModel @Inject constructor(
    private val authRepo: AuthRepo
) {

    private lateinit var viewModelScope: CoroutineScope

    // Using env vars for debug purpose only.
    private val _username = MutableStateFlow(System.getenv("PLAY_API_GOOGLE_USERNAME") ?: "")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow(System.getenv("PLAY_API_GOOGLE_PASSWORD") ?: "")
    val password: StateFlow<String> = _password

    private val _isUsernameError = MutableStateFlow(false)
    val isUsernameError: StateFlow<Boolean> = _isUsernameError

    private val _isPasswordError = MutableStateFlow(false)
    val isPasswordError: StateFlow<Boolean> = _isPasswordError

    private val _logInResponse = MutableStateFlow<Resource<Account>?>(null)
    val logInResponse: StateFlow<Resource<Account>?> = _logInResponse

    private var isSubmitted = false

    fun init(scope: CoroutineScope) {
        this.viewModelScope = scope
    }

    fun onUsernameChanged(newUsername: String) {
        _username.value = newUsername

        // Show error only if the form is submitted
        _isUsernameError.value = isSubmitted && isValidUsername(newUsername)
    }


    fun onPasswordChanged(newPassword: String) {
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

        viewModelScope.launch {
            authRepo.logIn(username, password)
                .onEach {
                    if (it is Resource.Success) {
                        authRepo.storeAccount(it.data)
                    }
                }
                .collect { logInResponse ->
                    _logInResponse.value = logInResponse
                }
        }
    }

    private fun isValidUsername(newUsername: String) = newUsername.isEmpty()
    private fun isValidPassword(newPassword: String) = newPassword.isEmpty()

}
