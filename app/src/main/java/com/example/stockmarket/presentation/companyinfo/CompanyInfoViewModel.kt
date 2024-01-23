package com.example.stockmarket.presentation.companyinfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarket.core.Resource
import com.example.stockmarket.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: StockRepository
): ViewModel() {

    private val _state: MutableStateFlow<CompanyInfoState> = MutableStateFlow(CompanyInfoState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            _state.update {
                it.copy(isLoading = true)
            }
            val companyInfoResult = async {
                repository.getCompanyInfo(symbol = symbol)
            }
            val intradayInfoResult = async {
                repository.getIntradayInfo(symbol = symbol)
            }

            when(val result = companyInfoResult.await()) {
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            company = null,
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            company = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            }

            when(val result = intradayInfoResult.await()) {
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            company = null,
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            stockInfos = result.data ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }
                }
            }
        }
    }
}