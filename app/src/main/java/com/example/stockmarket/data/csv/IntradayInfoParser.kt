package com.example.stockmarket.data.csv

import com.example.stockmarket.data.mapper.toIntradayInfo
import com.example.stockmarket.data.remote.dto.IntradayInfoDto
import com.example.stockmarket.domain.model.IntradayInfo
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntradayInfoParser @Inject constructor() : CSVParser<IntradayInfo> {

    override suspend fun parse(stream: InputStream): List<IntradayInfo> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull {
                    val timestamp = it.getOrNull(0) ?: return@mapNotNull null
                    val close = it.getOrNull(4) ?: return@mapNotNull null
                    val dto = IntradayInfoDto(
                        timestamp = timestamp,
                        close = close.toDouble()
                    )
                    dto.toIntradayInfo()
                }
                .filter {
                    it.timestamp.dayOfMonth == LocalDateTime.now().minusDays(1).dayOfMonth
                }
                .sortedBy {
                    it.timestamp.hour
                }
                .also {
                    csvReader.close()
                }
        }
    }
}