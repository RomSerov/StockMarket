package com.example.stockmarket.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stockmarket.data.local.entity.CompanyListingEntity

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListings(
        companyListing: List<CompanyListingEntity>
    )

    @Query("DELETE FROM companylistingentity")
    suspend fun clearCompanyListings()

    @Query("SELECT * FROM companylistingentity WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR UPPER(:query) == symbol")
    suspend fun searchCompanyListings(
        query: String
    ): List<CompanyListingEntity>
}