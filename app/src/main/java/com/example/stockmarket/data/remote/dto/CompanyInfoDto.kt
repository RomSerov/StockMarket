package com.example.stockmarket.data.remote.dto

import com.squareup.moshi.Json

data class CompanyInfoDto(
    @field:Json(name = "Symbol") val symbol: String? = null,
    @field:Json(name = "Description") val description: String? = null,
    @field:Json(name = "Name") val name: String? = null,
    @field:Json(name = "Country") val country: String? = null,
    @field:Json(name = "Industry") val industry: String? = null
)
