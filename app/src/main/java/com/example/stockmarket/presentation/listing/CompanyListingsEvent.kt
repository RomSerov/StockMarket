package com.example.stockmarket.presentation.listing

sealed class CompanyListingsEvent {
    data object Refresh: CompanyListingsEvent()
    data class OnSearchQueryChange(val query: String): CompanyListingsEvent()
}
