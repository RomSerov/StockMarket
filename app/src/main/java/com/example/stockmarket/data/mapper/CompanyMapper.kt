package com.example.stockmarket.data.mapper

import com.example.stockmarket.data.local.entity.CompanyListingEntity
import com.example.stockmarket.data.remote.dto.CompanyInfoDto
import com.example.stockmarket.domain.model.CompanyInfo
import com.example.stockmarket.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = this.name,
        symbol = this.symbol,
        exchange = this.exchange
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = this.name,
        symbol = this.symbol,
        exchange = this.exchange
    )
}

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = this.symbol ?: "",
        description = this.description ?: "",
        name = this.name ?: "",
        country = this.country ?: "",
        industry = this.industry ?: ""
    )
}