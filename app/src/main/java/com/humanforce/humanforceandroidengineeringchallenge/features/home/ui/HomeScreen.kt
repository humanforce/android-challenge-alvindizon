package com.humanforce.humanforceandroidengineeringchallenge.features.home.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsCell
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.humanforce.humanforceandroidengineeringchallenge.R
import com.humanforce.humanforceandroidengineeringchallenge.common.units.Temperature
import com.humanforce.humanforceandroidengineeringchallenge.common.units.toTemperatureString
import com.humanforce.humanforceandroidengineeringchallenge.data.locations.model.SavedLocationData
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.model.CurrentWeatherData
import com.humanforce.humanforceandroidengineeringchallenge.ui.common.LoadingOverlay
import com.humanforce.humanforceandroidengineeringchallenge.ui.common.MessageScreen
import com.humanforce.humanforceandroidengineeringchallenge.ui.common.SnackbarHandler
import com.humanforce.humanforceandroidengineeringchallenge.ui.common.WeatherCard
import com.humanforce.humanforceandroidengineeringchallenge.ui.theme.AppTheme
import com.humanforce.humanforceandroidengineeringchallenge.ui.utils.getOpenWeatherIconUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onSettingsClick: () -> Unit,
    onSearchClick: () -> Unit,
    onLocationClick: (SavedLocationData) -> Unit,
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
                        text = state.currentLocationName.orEmpty(),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription =
                            stringResource(R.string.settings_label)

                        )
                    }
                }
            )
        }
    )
    { paddingValues ->
        LoadingOverlay(isLoading = state.isLoading, modifier = Modifier.padding(paddingValues)) {
            HomeContent(
                modifier = Modifier.fillMaxSize(),
                state = state,
                onSearchClick = onSearchClick,
                onGrantPermissions = viewModel::onGrantPermissions,
                onSavedLocationCheck = viewModel::onSavedLocationCheck,
                onLocationClick = onLocationClick
            )
        }

    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun HomeContent(
    state: HomeUiState,
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit,
    onGrantPermissions: () -> Unit,
    onSavedLocationCheck: () -> Unit,
    onLocationClick: (SavedLocationData) -> Unit
) {
    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            locationPermissionState.allPermissionsGranted -> {
                LaunchedEffect(Unit) {
                    onGrantPermissions()
                }
            }

            locationPermissionState.shouldShowRationale -> {
                if (state.savedLocations.isNullOrEmpty()) {
                    PermissionRationaleView(
                        onSearchClick = onSearchClick,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    LaunchedEffect(Unit) {
                        onSavedLocationCheck()
                    }
                }
            }

            else -> {
                if (state.savedLocations.isNullOrEmpty()) {
                    PermissionNotGrantedView(
                        onLocationGrantClick = {
                            locationPermissionState.launchMultiplePermissionRequest()
                        },
                        onSearchClick = onSearchClick,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    LaunchedEffect(Unit) {
                        onSavedLocationCheck()
                    }
                }

            }
        }
        state.currentWeather?.let { currentWeather ->
            WeatherCard(
                onClick = {
                    onLocationClick(
                        SavedLocationData(
                            name = state.currentLocationName.orEmpty(),
                            latitude = state.currentLatitude ?: 0.0,
                            longitude = state.currentLongitude ?: 0.0,
                            country = "",
                            state = ""
                        )
                    )
                },
                modifier = Modifier.wrapContentSize(),
                iconUrl = currentWeather.icon.getOpenWeatherIconUrl(),
                description = currentWeather.description,
                temperatureString = currentWeather.temperature.toTemperatureString(Temperature.Celsius),
                feelsLikeTempString = currentWeather.feelsLike.toTemperatureString(Temperature.Celsius),
            )
        }
        // display other location forecasts as well if they exist
        if (state.locationsAndWeather.isNotEmpty()) {
            OtherLocationForecasts(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                savedLocations = state.locationsAndWeather,
                onLocationClick = onLocationClick
            )
        }
    }

}

@Composable
private fun PermissionNotGrantedView(
    onLocationGrantClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MessageScreen(
            message = stringResource(R.string.permission_not_granted_message)
        )
        Button(onClick = onLocationGrantClick) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = stringResource(R.string.grant_permission_label)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.grant_permission_label))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onSearchClick) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.go_to_search_label)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.go_to_search_label))
        }
    }
}


@Composable
private fun PermissionRationaleView(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MessageScreen(
            message = stringResource(R.string.permission_rationale_label),
        )
        Button(onClick = {
            context.startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${context.packageName}")
                )
            )
        }) {
            Icon(
                imageVector = Icons.Default.SettingsCell,
                contentDescription = stringResource(R.string.open_settings_label)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.open_settings_label))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onSearchClick) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.go_to_search_label)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.go_to_search_label))
        }
    }
}

@Composable
private fun OtherLocationForecasts(
    savedLocations: Map<SavedLocationData, CurrentWeatherData>,
    onLocationClick: (SavedLocationData) -> Unit,
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
            text = stringResource(R.string.other_locations_label),
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.titleLarge
        )
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(savedLocations.keys.toList()) { savedLocation ->
                OutlinedCard(onClick = { onLocationClick(savedLocation) }) {
                    Row {
                        ListItem(
                            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            headlineContent = {
                                Text(
                                    style = MaterialTheme.typography.bodyLarge,
                                    text = savedLocation.name,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            },
                            supportingContent = {
                                Text(
                                    style = MaterialTheme.typography.labelLarge,
                                    text = savedLocation.formattedLocationName,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            },
                            trailingContent = {
                                savedLocations[savedLocation]?.let {
                                    Text(
                                        text = it.temperature.toTemperatureString(Temperature.Celsius),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        HomeContent(
            state = HomeUiState.InitialState,
            onSearchClick = {},
            onGrantPermissions = {},
            onSavedLocationCheck = {},
            onLocationClick = {}
        )
    }
}