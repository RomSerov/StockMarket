package com.example.stockmarket.presentation.companyinfo

import com.example.stockmarket.domain.model.CompanyInfo
import com.example.stockmarket.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfos: List<IntradayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
