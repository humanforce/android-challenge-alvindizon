package com.humanforce.humanforceandroidengineeringchallenge.ui.common

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

/**
 * Responsible for displaying a Snackbar with the given [message]. [onShowSnackbar] is invoked after
 * displaying the snackbar.
 */
@Composable
fun SnackbarHandler(
    messageFlow: Flow<String>,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: () -> Unit
) {
    val latestOnShowSnackbar by rememberUpdatedState(onShowSnackbar)
    LaunchedEffect(Unit) {
        messageFlow.collectLatest {
            snackbarHostState.showSnackbar(it)
            latestOnShowSnackbar()
        }
    }
}