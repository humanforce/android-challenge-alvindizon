package com.humanforce.humanforceandroidengineeringchallenge.features.details.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.humanforce.humanforceandroidengineeringchallenge.ui.theme.AppTheme

@Composable
fun DetailsScreen(viewModel: DetailsViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DetailsContent(state = state)
}

@Composable
private fun DetailsContent(state: DetailsUiState, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "${state.locationName}, ${state.latitude}, ${state.longitude}")
    }
}

@Preview
@Composable
private fun DetailsScreenPreview() {
    AppTheme {
        DetailsContent(
            state = DetailsUiState(
                isLoading = false,
                locationName = "London",
                latitude = 51.5074,
                longitude = -0.1278
            )
        )
    }
}