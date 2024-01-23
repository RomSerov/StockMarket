package com.example.stockmarket.domain.model

import java.time.LocalDateTime

data class IntradayInfo(
    val timestamp: LocalDateTime,
    val close: Double
)
