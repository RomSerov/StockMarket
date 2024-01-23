package com.example.stockmarket.data.repository

import com.example.stockmarket.core.Resource
import com.example.stockmarket.data.csv.CSVParser
import com.example.stockmarket.data.csv.CompanyListingsParser
import com.example.stockmarket.data.csv.IntradayInfoParser
import com.example.stockmarket.data.local.StockDatabase
import com.example.stockmarket.data.mapper.toCompanyInfo
import com.example.stockmarket.data.mapper.toCompanyListing
import com.example.stockmarket.data.mapper.toCompanyListingEntity
import com.example.stockmarket.data.remote.StockApi
import com.example.stockmarket.domain.model.CompanyInfo
import com.example.stockmarket.domain.model.CompanyListing
import com.example.stockmarket.domain.model.IntradayInfo
import com.example.stockmarket.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingsParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfo>
) : StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))
            val localListings = dao.searchCompanyListings(query = query)
            emit(Resource.Success(
                data = localListings.map { it.toCompanyListing() }
            ))

            if (!(localListings.isEmpty() && query.isBlank()) && !fetchFromRemote) {
                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            val remoteListings = try {
                val response = api.getListings()
                companyListingsParser.parse(stream = response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Couldn't load data..."))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Couldn't load data..."))
                null
            }

            remoteListings?.let { list ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    companyListing = list.map { it.toCompanyListingEntity() }
                )
                emit(
                    Resource.Success(
                        data = dao.searchCompanyListings(query = "").map { it.toCompanyListing() }
                    )
                )
                emit(Resource.Loading(isLoading = false))

            }
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getIntradayInfo(symbol = symbol)
            val result = intradayInfoParser.parse(stream = response.byteStream())
            Resource.Success(data = result)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = "Couldn't load intraday info...")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = "Couldn't load intraday info...")
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val response = api.getCompanyInfo(symbol = symbol)
            Resource.Success(data = response.toCompanyInfo())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = "Couldn't load company info...")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = "Couldn't load company info...")
        }
    }
}