package com.example.stockmarket.data.mapper

import com.example.stockmarket.data.local.entity.CompanyListingEntity
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