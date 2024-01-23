package com.example.stockmarket.domain.repository

import com.example.stockmarket.core.Resource
import com.example.stockmarket.domain.model.CompanyInfo
import com.example.stockmarket.domain.model.CompanyListing
import com.example.stockmarket.domain.model.IntradayInfo
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>

    suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>>

    suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo>
}