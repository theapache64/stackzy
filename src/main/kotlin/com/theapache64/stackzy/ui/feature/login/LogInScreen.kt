package com.theapache64.stackzy.ui.feature.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.theapache64.gpa.model.Account
import com.theapache64.stackzy.data.util.calladapter.flow.Resource
import com.theapache64.stackzy.ui.common.CustomScaffold
import com.theapache64.stackzy.ui.common.ErrorSnackBar
import com.theapache64.stackzy.ui.common.loading.LoadingAnimation
import com.theapache64.stackzy.ui.theme.R
import com.theapache64.stackzy.ui.theme.StackzyTypography

@Composable
fun LogInScreen(
    viewModel: LogInScreenViewModel,
    onBackClicked: () -> Unit
) {

    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val isRemember by viewModel.isRemember.collectAsState()
    val isUsernameError by viewModel.isUsernameError.collectAsState()
    val isPasswordError by viewModel.isPasswordError.collectAsState()

    val logInResponse by viewModel.logInResponse.collectAsState()

    CustomScaffold(
        title = "Configure Profile",
        subTitle = "Login with your Google account",
        onBackClicked = onBackClicked
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            when (logInResponse) {

                is Resource.Loading -> {
                    LoadingAnimation(
                        message = "Authenticating...",
                        funFacts = null
                    )
                }

                is Resource.Success -> {
                    // Do nothing
                }

                null, is Resource.Error -> {
                    //Show form
                    Form(
                        username = username,
                        isUsernameError = isUsernameError,
                        password = password,
                        isRemember = isRemember,
                        isPasswordError = isPasswordError,
                        onUsernameChanged = viewModel::onUsernameChanged,
                        onPasswordChanged = viewModel::onPasswordChanged,
                        onLogInClicked = viewModel::onLogInClicked,
                        onCreateAccountClicked = viewModel::onCreateAccountClicked,
                        onRememberChanged = viewModel::onRememberChanged,
                        onRememberMeClicked = viewModel::onRememberClicked
                    )

                    if (logInResponse is Resource.Error) {
                        ErrorSnackBar(
                            syncFailedReason = (logInResponse as Resource.Error<Account>).errorData
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Form(
    username: String,
    isUsernameError: Boolean,
    password: String,
    isPasswordError: Boolean,
    isRemember: Boolean,
    onUsernameChanged: (username: String) -> Unit,
    onPasswordChanged: (password: String) -> Unit,
    onRememberChanged: (isRemember: Boolean) -> Unit,
    onRememberMeClicked: () -> Unit,
    onLogInClicked: () -> Unit,
    onCreateAccountClicked: () -> Unit
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .width(400.dp)
                .padding(20.dp)
        ) {


            // Warning: to protect your privacy, do not use your primary account, but 'register a secondary one' exclusively for use with
            // Stackzy.
            Text(
                text = getWarningText(),
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .background(
                        MaterialTheme.colors.secondary,
                        RoundedCornerShape(10.dp)
                    ).padding(12.dp)
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            val (usernameRef, passwordRef) = FocusRequester.createRefs()

            // Username
            OutlinedTextField(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = ""
                    )
                },
                singleLine = true,
                value = username,
                label = {
                    Text(
                        text = "Username",
                    )
                },
                onValueChange = onUsernameChanged,
                modifier = Modifier.fillMaxWidth()
                    .focusOrder(usernameRef) {
                        next = passwordRef
                    },
                isError = isUsernameError
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            // Password
            OutlinedTextField(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Password,
                        contentDescription = "Password"
                    )
                },
                singleLine = true,
                value = password,
                label = {
                    Text(
                        text = "Password",
                    )
                },
                onValueChange = onPasswordChanged,
                modifier = Modifier.fillMaxWidth().focusOrder(passwordRef),
                visualTransformation = if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                isError = isPasswordError,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            isPasswordVisible = !isPasswordVisible
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Visibility,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = isRemember,
                    onCheckedChange = onRememberChanged,
                    colors = CheckboxDefaults.colors(checkedColor = R.color.TelegramBlue)
                )
                Text(
                    text = "Remember Me",
                    style = StackzyTypography.body2,
                    modifier = Modifier.clickable {
                        onRememberMeClicked()
                    }
                )
            }


            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // Button
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onLogInClicked
            ) {
                Text(text = "LOGIN")
            }

            // Create account button
            Text(
                text = "CREATE ACCOUNT",
                style = MaterialTheme.typography.button,
                modifier = Modifier
                    .padding(top = 10.dp) // top margin
                    .clickable {
                        onCreateAccountClicked()
                    }
                    .padding(15.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )

            DisposableEffect(Unit) {
                usernameRef.requestFocus()
                onDispose { }
            }
        }

    }
}

@Composable
fun getWarningText(): AnnotatedString {
    return with(AnnotatedString.Builder("")) {

        // Red
        pushStyle(SpanStyle(color = R.color.YellowGreen))
        append("IMPORTANT: ")

        // Normal content color
        pushStyle(SpanStyle(color = MaterialTheme.colors.onSurface.copy(0.8f)))
        append("To protect your privacy, DO NOT use your primary account, but register a secondary one exclusively for Stackzy.")

        toAnnotatedString()
    }
}