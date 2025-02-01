package com.humanforce.humanforceandroidengineeringchallenge.features.search.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.humanforce.humanforceandroidengineeringchallenge.features.search.model.SearchResult
import com.humanforce.humanforceandroidengineeringchallenge.features.search.usecase.SearchLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SearchLocationUiState(
    val isLoading: Boolean = false,
    val searchResults: List<SearchResult>? = null
) {
    companion object {
        val InitialState = SearchLocationUiState()
    }
}

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val searchLocationUseCase: SearchLocationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchLocationUiState.InitialState)
    val uiState = _uiState.asStateFlow()

    val searchQueryFlow = savedStateHandle.getStateFlow(key = QUERY_KEY, initialValue = "")

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _uiState.update { state -> state.copy(isLoading = false) }
    }

    init {
        viewModelScope.launch(coroutineExceptionHandler) {
            searchQueryFlow
                .debounce(DEBOUNCE_MS)
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .onEach { search(it) }
                .collect()
        }
    }

    private fun search(query: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            runCatching {
                searchLocationUseCase.invoke(query)
            }.onSuccess {
                _uiState.update { state -> state.copy(isLoading = false, searchResults = it) }
            }.onFailure {
                _uiState.update { state -> state.copy(isLoading = false) }
            }
        }
    }

    fun onQueryChange(query: String) {
        savedStateHandle[QUERY_KEY] = query
        _uiState.value = SearchLocationUiState(isLoading = query.isNotBlank())
    }

    companion object {
        const val QUERY_KEY = "searchQuery"
        private const val DEBOUNCE_MS = 500L
    }

}