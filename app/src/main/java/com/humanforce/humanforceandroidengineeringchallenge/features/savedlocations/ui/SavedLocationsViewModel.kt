package com.humanforce.humanforceandroidengineeringchallenge.features.savedlocations.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanforce.humanforceandroidengineeringchallenge.data.locations.LocationsRepository
import com.humanforce.humanforceandroidengineeringchallenge.data.locations.model.SavedLocationData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.plus
import javax.inject.Inject


data class SavedLocationsUiState(
    val isLoading: Boolean = false,
    val locations: List<SavedLocationData>? = null
) {
    companion object {
        val InitialState = SavedLocationsUiState()
    }
}

@HiltViewModel
class SavedLocationsViewModel @Inject constructor(
    repository: LocationsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavedLocationsUiState.InitialState)
    val uiState = _uiState.asStateFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _uiState.update { state -> state.copy(isLoading = false) }
    }

    init {
        repository.getAllLocations()
            .onEach { _uiState.update { state -> state.copy(isLoading = false, locations = it) } }
            .launchIn(viewModelScope + coroutineExceptionHandler)
    }
}