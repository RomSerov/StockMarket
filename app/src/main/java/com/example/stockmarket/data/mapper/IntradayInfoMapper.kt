package com.example.stockmarket.data.mapper

import com.example.stockmarket.data.remote.dto.IntradayInfoDto
import com.example.stockmarket.domain.model.IntradayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun IntradayInfoDto.toIntradayInfo(): IntradayInfo {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(timestamp, formatter)
    return IntradayInfo(
        timestamp = localDateTime,
        close = close ?: 0.0
    )
}