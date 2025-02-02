package com.humanforce.humanforceandroidengineeringchallenge.features.savedlocations.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.humanforce.humanforceandroidengineeringchallenge.R
import com.humanforce.humanforceandroidengineeringchallenge.data.locations.model.SavedLocationData
import com.humanforce.humanforceandroidengineeringchallenge.ui.common.LoadingOverlay
import com.humanforce.humanforceandroidengineeringchallenge.ui.common.MessageScreen
import com.humanforce.humanforceandroidengineeringchallenge.ui.theme.AppTheme
import com.humanforce.humanforceandroidengineeringchallenge.ui.utils.toFlagEmoji

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedLocationsScreen(
    viewModel: SavedLocationsViewModel,
    onNavigationIconClick: () -> Unit,
    onSavedLocationClick: (SavedLocationData) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp),
                title = {
                    Text(
                        text = stringResource(R.string.saved_locations_label),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LoadingOverlay(
            modifier = Modifier.padding(paddingValues),
            isLoading = state.isLoading,
        ) {
            SavedLocationsContent(state = state, onSavedLocationClick = onSavedLocationClick)
        }
    }
}

@Composable
private fun SavedLocationsContent(
    state: SavedLocationsUiState,
    onSavedLocationClick: (SavedLocationData) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.locations.isNullOrEmpty()) {
        MessageScreen(
            message = stringResource(R.string.no_saved_locations),
            modifier = modifier.fillMaxSize()
        )
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.locations) {
                OutlinedCard(onClick = { onSavedLocationClick(it) }) {
                    ListItem(
                        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        headlineContent = {
                            Text(
                                style = MaterialTheme.typography.bodyLarge,
                                text = it.name,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        },
                        supportingContent = {
                            val text = if (it.state.isBlank()) {
                                "${it.country} ${it.country.toFlagEmoji()}"
                            } else {
                                "${it.state}, ${it.country} ${it.country.toFlagEmoji()}"
                            }
                            Text(
                                style = MaterialTheme.typography.labelLarge,
                                text = text,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SavedLocationsContentPreview() {
    val state = SavedLocationsUiState(
        isLoading = false,
        locations = listOf(
            SavedLocationData(
                name = "San Pedro",
                state = "Laguna",
                country = "PH",
                latitude = 1.0,
                longitude = 1.0
            )
        )
    )
    AppTheme {
        SavedLocationsContent(state = state, onSavedLocationClick = {})
    }
}