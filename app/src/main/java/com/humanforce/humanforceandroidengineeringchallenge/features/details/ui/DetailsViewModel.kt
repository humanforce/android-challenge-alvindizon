package com.humanforce.humanforceandroidengineeringchallenge.features.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.humanforce.humanforceandroidengineeringchallenge.navigation.DetailsDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class DetailsUiState(
    val isLoading: Boolean = false,
    val locationName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
) {
    companion object {
        val InitialState = DetailsUiState()
    }
}

@HiltViewModel
class DetailsViewModel @Inject constructor(savedStateHandle: SavedStateHandle) :
    ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState.InitialState)
    val uiState = _uiState.asStateFlow()

    init {
        val args = savedStateHandle.toRoute<DetailsDestination>()
        _uiState.update {
            it.copy(
                locationName = args.locationName,
                latitude = args.latitude,
                longitude = args.longitude
            )
        }
    }
}