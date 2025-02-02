package com.humanforce.humanforceandroidengineeringchallenge.features.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.humanforce.humanforceandroidengineeringchallenge.features.details.model.AggregateDailyForecast
import com.humanforce.humanforceandroidengineeringchallenge.features.details.model.CurrentWeather
import com.humanforce.humanforceandroidengineeringchallenge.features.details.usecase.GetForecastDetailsUseCase
import com.humanforce.humanforceandroidengineeringchallenge.navigation.DetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class DetailsUiState(
    val isLoading: Boolean = false,
    val locationName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val currentWeather: CurrentWeather? = null,
    val dailyForecasts: List<AggregateDailyForecast>? = null
) {
    companion object {
        val InitialState = DetailsUiState()
    }
}

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getForecastDetailsUseCase: GetForecastDetailsUseCase
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState.InitialState)
    val uiState = _uiState.asStateFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _uiState.update { state -> state.copy(isLoading = false) }
    }

    init {
        val args = savedStateHandle.toRoute<DetailsDestination>()
        _uiState.update {
            it.copy(
                locationName = args.locationName,
                latitude = args.latitude,
                longitude = args.longitude
            )
        }
        loadForecast(args.latitude, args.longitude)
    }

    private fun loadForecast(latitude: Double, longitude: Double) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(coroutineExceptionHandler) {
            runCatching {
                getForecastDetailsUseCase.invoke(latitude, longitude)
            }.onSuccess { (currentWeather, dailyForecasts) ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentWeather = currentWeather,
                        dailyForecasts = dailyForecasts
                    )
                }
            }.onFailure { _uiState.update { it.copy(isLoading = false) } }
        }
    }
}