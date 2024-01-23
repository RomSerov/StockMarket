package com.example.stockmarket.presentation.companyinfo

import com.example.stockmarket.domain.model.CompanyInfo
import com.example.stockmarket.domain.model.IntradayInfo
import java.time.LocalDateTime

fun getMockCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = "AA",
        description = "Alcoa Corporation produces and sells bauxite, alumina, and aluminum products in the United States, Spain, Australia, Brazil, Canada, and internationally. The company is headquartered in Pittsburgh, Pennsylvania.",
        name = "Alcoa Corp",
        country = "USA",
        industry = "PRIMARY PRODUCTION OF ALUMINUM"
    )
}

fun getMockStockInfos(): List<IntradayInfo> {
    return listOf(
        IntradayInfo(timestamp = LocalDateTime.parse("2024-01-22T04:00"), close=27.19),
        IntradayInfo(timestamp = LocalDateTime.parse("2024-01-22T05:00"), close=26.9),
        IntradayInfo(timestamp = LocalDateTime.parse("2024-01-22T06:00"), close=26.98),
        IntradayInfo(timestamp = LocalDateTime.parse("2024-01-22T07:00"), close=27.1),
        IntradayInfo(timestamp = LocalDateTime.parse("2024-01-22T08:00"), close=27.04),
        IntradayInfo(timestamp = LocalDateTime.parse("2024-01-22T09:00"), close=27.71),
        IntradayInfo(timestamp = LocalDateTime.parse("2024-01-22T10:00"), close=27.31),
        IntradayInfo(timestamp = LocalDateTime.parse("2024-01-22T11:00"), close=27.755),
        IntradayInfo(timestamp = LocalDateTime.parse("2024-01-22T12:00"), close=27.735),
        IntradayInfo(timestamp = LocalDateTime.parse("2024-01-22T13:00"), close=27.555),
        IntradayInfo(timestamp = LocalDateTime.parse("2024-01-22T14:00"), close=27.6),
        IntradayInfo(timestamp = LocalDateTime.parse("2024-01-22T15:00"), close=27.61),
        IntradayInfo(timestamp = LocalDateTime.parse("2024-01-22T16:00"), close=27.63),
        IntradayInfo(timestamp = LocalDateTime.parse("2024-01-22T17:00"), close=27.63),
        IntradayInfo(timestamp = LocalDateTime.parse("2024-01-22T18:00"), close=27.669),
        IntradayInfo(timestamp = LocalDateTime.parse("2024-01-22T19:00"), close=27.67)
    )
}