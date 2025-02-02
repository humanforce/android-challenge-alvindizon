package com.humanforce.humanforceandroidengineeringchallenge.features.details.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.humanforce.humanforceandroidengineeringchallenge.R
import com.humanforce.humanforceandroidengineeringchallenge.common.units.Temperature
import com.humanforce.humanforceandroidengineeringchallenge.common.units.toTemperatureString
import com.humanforce.humanforceandroidengineeringchallenge.features.details.model.AggregateDailyForecast
import com.humanforce.humanforceandroidengineeringchallenge.features.details.model.CurrentWeather
import com.humanforce.humanforceandroidengineeringchallenge.ui.common.LoadingOverlay
import com.humanforce.humanforceandroidengineeringchallenge.ui.common.SnackbarHandler
import com.humanforce.humanforceandroidengineeringchallenge.ui.theme.AppTheme
import com.humanforce.humanforceandroidengineeringchallenge.ui.utils.getOpenWeatherIconUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    SnackbarHandler(
        messageFlow = viewModel.snackbarMessage,
        snackbarHostState = snackbarHostState,
        onShowSnackbar = {}
    )
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp),
                title = {
                    Text(
                        text = state.locationName.orEmpty(),
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
                },
                actions = {
                    IconButton(onClick = viewModel::onSaveLocationClick) {
                        Icon(
                            imageVector = if (state.isLocationSaved) {
                                Icons.Default.BookmarkAdded
                            } else {
                                Icons.Default.BookmarkAdd
                            },
                            contentDescription = if (state.isLocationSaved) {
                                stringResource(R.string.remove_location_label)
                            } else {
                                stringResource(R.string.save_location_label)
                            }
                        )
                    }
                }
            )
        }
    ) { padding ->
        LoadingOverlay(isLoading = state.isLoading, modifier = Modifier.padding(padding)) {
            DetailsContent(
                state = state,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun DetailsContent(state: DetailsUiState, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        // Main Content: Contains max temp, min temp, feels like, weather icon
        state.currentWeather?.let { WeatherCard(it) }
        // Daily forecasts: contains 5-day forecast with aggregated min/max temps.
        state.dailyForecasts?.let {
            DailyForecast(
                dailyForecasts = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun WeatherCard(currentWeather: CurrentWeather, modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors()
            .copy(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = currentWeather.icon.getOpenWeatherIconUrl(),
                    contentDescription = currentWeather.description,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(100.dp)
                )
                Text(
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displaySmall,
                    text = currentWeather.description,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.displayMedium,
                    text = currentWeather.temperature.toTemperatureString(Temperature.Celsius),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = stringResource(
                        R.string.feels_like_label,
                        currentWeather.feelsLike.toTemperatureString(Temperature.Celsius),
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun DailyForecast(
    dailyForecasts: List<AggregateDailyForecast>,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.outlinedCardColors()
            .copy(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
    ) {
        Text(
            text = stringResource(R.string.daily_forecast_label),
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.titleLarge
        )
        LazyVerticalStaggeredGrid(
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            columns = StaggeredGridCells.Adaptive(128.dp),
            verticalItemSpacing = 8.dp
        ) {
            items(dailyForecasts) { item ->
                OutlinedCard(
                    colors = CardDefaults.outlinedCardColors()
                        .copy(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .align(Alignment.CenterHorizontally),
                        text = item.formattedDate,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    AsyncImage(
                        model = item.icon.getOpenWeatherIconUrl(),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 4.dp)
                    )
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = stringResource(R.string.min_label, item.minTemperatureString),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = stringResource(R.string.max_label, item.maxTemperatureString),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
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