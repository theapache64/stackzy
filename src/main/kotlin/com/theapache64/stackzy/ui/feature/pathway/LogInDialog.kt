package com.theapache64.stackzy.ui.feature.pathway

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Password
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.ui.theme.StackzyTheme

fun main(args: Array<String>) {
    LogInDialog { username, password ->
    }
}

// TODO : Use loadingMessage to hide content and show loading message
fun LogInDialog(
    loadingMessage: String? = null,
    onSubmit: (username: String, password: String) -> Unit
) {

    Window(
        title = "LogIn",
        size = IntSize(400, 600),
        undecorated = true
    ) {

        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isSubmitted by remember { mutableStateOf(false) }

        val currentWindow = LocalAppWindow.current


        StackzyTheme(
            displayToolbar = true
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {

                // Configure Profile
                Text(
                    text = "Configure Profile",
                    style = MaterialTheme.typography.h5
                )


                // Please login with your google account
                Text(
                    text = "Please login with your google account",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface.copy(0.5f)
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

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
                    onValueChange = { newUsername ->
                        username = newUsername
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = isSubmitted && username.isEmpty()
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                // Password
                OutlinedTextField(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Password,
                            contentDescription = ""
                        )
                    },
                    singleLine = true,
                    value = password,
                    label = {
                        Text(
                            text = "Password",
                        )
                    },
                    onValueChange = { newPassword ->
                        password = newPassword
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    isError = isSubmitted && password.isEmpty()
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                // Button
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        isSubmitted = true

                        if (username.isBlank().not() && password.isBlank().not()) {
                            onSubmit(username, password)
                            currentWindow.close()
                        }
                    }
                ) {
                    Text(text = "LOGIN")
                }
            }
        }
    }
}

@Composable
fun getWarningText(): AnnotatedString {
    return with(AnnotatedString.Builder("")) {

        // Red
        pushStyle(SpanStyle(color = MaterialTheme.colors.error))
        append("IMPORTANT: ")

        // Normal content color
        pushStyle(SpanStyle(color = MaterialTheme.colors.onSurface.copy(0.8f)))
        append("To protect your privacy, DO NOT use your primary account, but register a secondary one exclusively for Stackzy.")

        toAnnotatedString()
    }
}