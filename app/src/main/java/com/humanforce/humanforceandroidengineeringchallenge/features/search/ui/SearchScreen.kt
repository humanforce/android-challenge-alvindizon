package com.humanforce.humanforceandroidengineeringchallenge.features.search.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.humanforce.humanforceandroidengineeringchallenge.R
import com.humanforce.humanforceandroidengineeringchallenge.features.search.model.SearchResult
import com.humanforce.humanforceandroidengineeringchallenge.ui.common.MessageScreen
import com.humanforce.humanforceandroidengineeringchallenge.ui.utils.toFlagEmoji

@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel,
    onSearchResultClick: (SearchResult) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQueryFlow.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = searchQuery,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_placeholder_label),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_placeholder_label),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                onValueChange = viewModel::onQueryChange,
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                viewModel.onQueryChange("")
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.clear_search_label),
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                },
                singleLine = true
            )
        }) { paddingValues ->
        SearchResultContent(
            state = state,
            searchQuery = searchQuery,
            onSearchResultClick = onSearchResultClick,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun SearchResultContent(
    state: SearchLocationUiState,
    searchQuery: String,
    onSearchResultClick: (SearchResult) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
        if (state.searchResults.isNullOrEmpty()) {
            val message = when {
                state.searchResults?.isEmpty() == true -> stringResource(
                    R.string.no_results_found,
                    searchQuery
                )

                else -> stringResource(R.string.search_initial_label)
            }
            MessageScreen(message = message, modifier = Modifier.fillMaxSize())
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // No key provided here, some locations have duplicate results
                items(state.searchResults) {
                    OutlinedCard(onClick = { onSearchResultClick(it) }) {
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
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenScreenPreview() {
    val searchResults = listOf(
        SearchResult(
            name = "San Pedro",
            state = "Laguna",
            country = "PH",
            lat = 1.0,
            lon = 1.0
        ),
        SearchResult(
            name = "Santa Rosa",
            state = "Laguna",
            country = "PH",
            lat = 1.0,
            lon = 1.0
        ),
        SearchResult(
            name = "Biñan",
            state = "Laguna",
            country = "PH",
            lat = 1.0,
            lon = 1.0
        )
    )
    val state = SearchLocationUiState(
        isLoading = false,
        searchResults = searchResults
    )
    SearchResultContent(
        state = state,
        searchQuery = "",
        onSearchResultClick = {},
    )
}