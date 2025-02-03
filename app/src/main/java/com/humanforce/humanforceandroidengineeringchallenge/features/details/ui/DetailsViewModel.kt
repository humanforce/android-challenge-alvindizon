package com.humanforce.humanforceandroidengineeringchallenge.features.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.humanforce.humanforceandroidengineeringchallenge.common.units.MeasurementUnit
import com.humanforce.humanforceandroidengineeringchallenge.data.locations.LocationsRepository
import com.humanforce.humanforceandroidengineeringchallenge.data.locations.model.SavedLocationData
import com.humanforce.humanforceandroidengineeringchallenge.data.settings.SettingsDataStore
import com.humanforce.humanforceandroidengineeringchallenge.data.weather.model.CurrentWeatherData
import com.humanforce.humanforceandroidengineeringchallenge.features.details.model.AggregateDailyForecast
import com.humanforce.humanforceandroidengineeringchallenge.features.details.model.CurrentWeatherDetails
import com.humanforce.humanforceandroidengineeringchallenge.features.details.usecase.GetForecastDetailsUseCase
import com.humanforce.humanforceandroidengineeringchallenge.navigation.DetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject


data class DetailsUiState(
    val isLoading: Boolean = false,
    val locationName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val currentWeather: CurrentWeatherDetails? = null,
    val dailyForecasts: List<AggregateDailyForecast>? = null,
    val isLocationSaved: Boolean = false,
    val country: String? = null,
    val state: String? = null
) {
    companion object {
        val InitialState = DetailsUiState()
    }
}

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getForecastDetailsUseCase: GetForecastDetailsUseCase,
    private val locationsRepository: LocationsRepository,
    settingsDataStore: SettingsDataStore
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState.InitialState)
    val uiState = _uiState.asStateFlow()

    private val _snackbarChannel = Channel<String>(Channel.CONFLATED)
    val snackbarMessage = _snackbarChannel.receiveAsFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.update { state -> state.copy(isLoading = false) }
        _snackbarChannel.trySend(exception.message.orEmpty())
    }

    init {
        val args = savedStateHandle.toRoute<DetailsDestination>()
        _uiState.update {
            it.copy(
                locationName = args.locationName,
                latitude = args.latitude,
                longitude = args.longitude,
                country = args.country,
                state = args.state
            )
        }
        settingsDataStore.getUnits()
            .onEach {
                loadForecast(args.latitude, args.longitude, it)
            }
            .launchIn(viewModelScope + coroutineExceptionHandler)
        checkIfLocationIsSaved(args.latitude, args.longitude)
    }

    private fun checkIfLocationIsSaved(latitude: Double, longitude: Double) {
        viewModelScope.launch(coroutineExceptionHandler) {
            runCatching {
                locationsRepository.isLocationSaved(latitude, longitude)
            }.onSuccess { isSaved ->
                _uiState.update { it.copy(isLocationSaved = isSaved) }
            }
        }
    }

    fun onSaveLocationClick() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val latitude = _uiState.value.latitude
            val longitude = _uiState.value.longitude

            if (latitude != null && longitude != null) {
                if (_uiState.value.isLocationSaved) {
                    runCatching {
                        locationsRepository.deleteLocation(latitude, longitude)
                    }.onSuccess {
                        _snackbarChannel.trySend("Location removed.")
                    }

                } else {
                    runCatching{
                        locationsRepository.saveLocation(
                            SavedLocationData(
                                name = _uiState.value.locationName.orEmpty(),
                                state = _uiState.value.state.orEmpty(),
                                country = _uiState.value.country.orEmpty(),
                                latitude = latitude,
                                longitude = longitude
                            )
                        )
                    }.onSuccess {
                        _snackbarChannel.trySend("Location saved.")
                    }
                }
                checkIfLocationIsSaved(latitude, longitude)
            }
        }
    }

    private fun loadForecast(latitude: Double, longitude: Double, unit: MeasurementUnit) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(coroutineExceptionHandler) {
            runCatching {
                getForecastDetailsUseCase.invoke(latitude, longitude, unit)
            }.onSuccess { (currentWeather, dailyForecasts) ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentWeather = currentWeather,
                        dailyForecasts = dailyForecasts
                    )
                }
            }.onFailure {
                _uiState.update { state -> state.copy(isLoading = false) }
                _snackbarChannel.trySend(it.message.orEmpty())
            }
        }
    }
}