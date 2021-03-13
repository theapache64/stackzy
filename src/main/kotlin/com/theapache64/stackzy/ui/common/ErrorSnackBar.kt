package com.theapache64.stackzy.ui.common

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.util.R

/**
 * To show an error SnackBar with retry button at bottom of the screen.
 */
@Composable
fun BoxScope.ErrorSnackBar(
    syncFailedReason: String,
    onRetryClicked: (() -> Unit)? = null,
) {
    Snackbar(
        modifier = Modifier
            .padding(10.dp)
            .align(Alignment.BottomCenter),
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface,
        action = {
            if (onRetryClicked != null) {
                TextButton(
                    onClick = onRetryClicked
                ) {
                    Text(R.string.all_action_retry)
                }
            }
        }
    ) {
        Text(text = syncFailedReason)
    }
}