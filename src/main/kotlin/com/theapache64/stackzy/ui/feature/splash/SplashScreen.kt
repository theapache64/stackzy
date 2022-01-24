package com.theapache64.stackzy.ui.feature.splash

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theapache64.stackzy.ui.common.ErrorSnackBar
import com.theapache64.stackzy.ui.common.LoadingText
import com.theapache64.stackzy.ui.common.Logo

/**
 * Renders SplashScreen
 */
@ExperimentalFoundationApi
@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel,
    onSyncFinished: () -> Unit,
    onUpdateNeeded: () -> Unit
) {

    val isSyncFinished by splashViewModel.isSyncFinished.collectAsState()
    val syncFailedReason by splashViewModel.syncFailedMsg.collectAsState()
    val syncMessage by splashViewModel.syncMsg.collectAsState()
    val shouldUpdate by splashViewModel.shouldUpdate.collectAsState(initial = false)

    LaunchedEffect(shouldUpdate) {
        if (shouldUpdate) {
            onUpdateNeeded()
        }
    }

    if (isSyncFinished) {
        onSyncFinished()
        return
    }

    // Content
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Logo
        Logo(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
        )

        if (isSyncFinished.not()) {

            // Loading text
            LoadingText(
                message = syncMessage,
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .align(Alignment.BottomCenter)

            )
        }

        syncFailedReason?.let {
            ErrorSnackBar(
                syncFailedReason = it,
                onRetryClicked = {
                    splashViewModel.onRetryClicked()
                }
            )
        }
    }
}



