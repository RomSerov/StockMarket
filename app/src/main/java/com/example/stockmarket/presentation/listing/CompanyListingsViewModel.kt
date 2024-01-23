package com.example.stockmarket.presentation.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarket.core.Resource
import com.example.stockmarket.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListingsViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CompanyListingsState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        getCompanyListings()
    }

    fun onEvent(event: CompanyListingsEvent) {
        when (event) {
            is CompanyListingsEvent.OnSearchQueryChange -> {
                _state.update { state ->
                    state.copy(searchQuery = event.query)
                }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanyListings()
                }
            }

            CompanyListingsEvent.Refresh -> {
                getCompanyListings(
                    fetchFromRemote = true
                )
            }
        }
    }

    private fun getCompanyListings(
        query: String = state.value.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            repository
                .getCompanyListings(
                    fetchFromRemote = fetchFromRemote,
                    query = query
                )
                .collect {
                    when (it) {
                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            _state.update { state ->
                                state.copy(
                                    isLoading = it.isLoading
                                )
                            }
                        }

                        is Resource.Success -> {
                            it.data?.let { list ->
                                _state.update { state ->
                                    state.copy(
                                        companies = list
                                    )
                                }
                            }
                        }
                    }
                }
        }
    }
}