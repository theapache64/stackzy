package com.theapache64.stackzy.ui.feature.update

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.theapache64.stackzy.App
import com.theapache64.stackzy.ui.common.FullScreenError

@Composable
fun UpdateScreen(
    viewModel: UpdateScreenViewModel
) {
    FullScreenError(
        title = "Update",
        message = "Looks like you're using an older version of ${App.appArgs.appName}." +
                  "Please download the latest version and update. Thank you :)",
        action = {
            Button(
                onClick = {
                    viewModel.onUpdateClicked()
                }
            ) {
                Text(text = "UPDATE")
            }
        }
    )
}