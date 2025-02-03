package com.humanforce.humanforceandroidengineeringchallenge.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanforce.humanforceandroidengineeringchallenge.common.units.MeasurementUnit
import com.humanforce.humanforceandroidengineeringchallenge.data.locations.LocationsRepository
import com.humanforce.humanforceandroidengineeringchallenge.data.locations.model.SavedLocationData
import com.humanforce.humanforceandroidengineeringchallenge.data.settings.SettingsDataStore
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.WeatherRepository
import com.humanforce.humanforceandroidengineeringchallenge.features.home.mapper.toCurrentLocationWeather
import com.humanforce.humanforceandroidengineeringchallenge.features.home.model.CurrentLocationWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val isLocationPermissionGranted: Boolean = false,
    val savedLocations: List<SavedLocationData>? = null,
    val locationsAndWeather: Map<SavedLocationData, CurrentLocationWeather> = mutableMapOf(),
    val currentLocationName: String? = null,
    val currentWeather: CurrentLocationWeather? = null,
    val currentLatitude: Double? = null,
    val currentLongitude: Double? = null
) {
    companion object {
        val InitialState = HomeUiState()
    }
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationsRepository: LocationsRepository,
    private val weatherRepository: WeatherRepository,
    private val settingsDataStore: SettingsDataStore
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState.InitialState)
    val uiState = _uiState.asStateFlow()

    private val _snackbarChannel = Channel<String>(Channel.CONFLATED)
    val snackbarMessage = _snackbarChannel.receiveAsFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.update { state -> state.copy(isLoading = false) }
        _snackbarChannel.trySend(exception.message.orEmpty())
    }

    init {
        combine(
            locationsRepository.getAllLocations(),
            settingsDataStore.getUnits()
        ) { locations, units ->
            fetchForecastForOtherLocations(locations, units)
        }
            .launchIn(viewModelScope + coroutineExceptionHandler)
    }

    fun onGrantPermissions() {
        _uiState.update { state -> state.copy(isLoading = true) }
        viewModelScope.launch(coroutineExceptionHandler) {
            settingsDataStore.getUnits().collectLatest { unit ->
                runCatching {
                    locationsRepository.getCurrentLocation()
                }.onSuccess { currentLocationData ->
                    currentLocationData?.let {
                        _uiState.update { state -> state.copy(currentLocationName = it.name) }
                        fetchCurrentWeather(
                            latitude = it.latitude,
                            longitude = it.longitude,
                            unit = unit
                        )
                    }
                }.onFailure {
                    _uiState.update { state -> state.copy(isLoading = false) }
                    _snackbarChannel.trySend(it.message.orEmpty())
                }
            }

        }
    }

    // If there are existing locations and users denies locations permissions,
    // just use the first saved location as the current weather
    fun onSavedLocationCheck() {
        _uiState.update { state -> state.copy(isLoading = true) }
        val firstLocation = _uiState.value.savedLocations?.firstOrNull()
        viewModelScope.launch(coroutineExceptionHandler) {
            _uiState.update { it.copy(currentLocationName = firstLocation?.name.orEmpty()) }
            settingsDataStore.getUnits()
                .collectLatest { unit ->
                    firstLocation?.let {
                        fetchCurrentWeather(
                            latitude = it.latitude,
                            longitude = it.longitude,
                            unit = unit
                        )
                    }
                }
        }
    }

    private suspend fun fetchCurrentWeather(
        latitude: Double,
        longitude: Double,
        unit: MeasurementUnit
    ) {
        runCatching {
            weatherRepository.getCurrentWeather(latitude, longitude, unit.name)
        }.onSuccess { currentWeather ->
            _uiState.update {
                it.copy(
                    currentWeather = currentWeather.toCurrentLocationWeather(unit),
                    currentLatitude = latitude,
                    currentLongitude = longitude,
                    isLoading = false
                )
            }
        }.onFailure {
            _uiState.update { state -> state.copy(isLoading = false) }
            _snackbarChannel.trySend(it.message.orEmpty())
        }
    }


    private suspend fun fetchForecastForOtherLocations(
        savedLocations: List<SavedLocationData>,
        units: MeasurementUnit
    ) {
        _uiState.update { it.copy(savedLocations = savedLocations) }
        savedLocations.forEach { savedLocation ->
            runCatching {
                weatherRepository.getCurrentWeather(
                    savedLocation.latitude,
                    savedLocation.longitude,
                    units.name
                )
            }.onSuccess { currentWeather ->
                val current = _uiState.value.locationsAndWeather
                _uiState.update {
                    it.copy(
                        locationsAndWeather = current.plus(
                            savedLocation to currentWeather.toCurrentLocationWeather(
                                units
                            )
                        ),
                        isLoading = false
                    )
                }
            }.onFailure {
                _uiState.update { state -> state.copy(isLoading = false) }
                _snackbarChannel.trySend(it.message.orEmpty())
            }
        }
    }
}