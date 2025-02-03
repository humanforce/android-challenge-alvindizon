package com.humanforce.humanforceandroidengineeringchallenge.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanforce.humanforceandroidengineeringchallenge.common.units.MeasurementUnit
import com.humanforce.humanforceandroidengineeringchallenge.data.settings.SettingsDataStore
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

data class SettingsUiState(
    val isLoading: Boolean = false,
    val preferredMeasurementUnit: MeasurementUnit? = null
) {
    companion object {
        val InitialState = SettingsUiState()
    }
}

@HiltViewModel
class SettingsViewModel @Inject constructor(private val settingsDataStore: SettingsDataStore) :
    ViewModel() {
    private val _snackbarChannel = Channel<String>(Channel.CONFLATED)
    val snackbarMessage = _snackbarChannel.receiveAsFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.update { state -> state.copy(isLoading = false) }
        _snackbarChannel.trySend(exception.message.orEmpty())
    }

    private val _uiState = MutableStateFlow(SettingsUiState.InitialState)

    val uiState = _uiState.asStateFlow()
    init {
        _uiState.update { it.copy(isLoading = true) }
        settingsDataStore.getUnits()
            .onEach {
                _uiState.update { state ->
                    state.copy(isLoading = false, preferredMeasurementUnit = it)
                }
            }
            .launchIn(viewModelScope + coroutineExceptionHandler)
    }

    fun onUnitClick(measurementUnit: MeasurementUnit) {
        viewModelScope.launch(coroutineExceptionHandler) {
            _uiState.update { it.copy(preferredMeasurementUnit = measurementUnit) }
            settingsDataStore.setUnits(measurementUnit)
        }
    }
}
