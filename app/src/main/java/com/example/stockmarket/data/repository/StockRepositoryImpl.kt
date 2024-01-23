package com.example.stockmarket.data.repository

import com.example.stockmarket.core.Resource
import com.example.stockmarket.data.csv.CSVParser
import com.example.stockmarket.data.csv.CompanyListingsParser
import com.example.stockmarket.data.local.StockDatabase
import com.example.stockmarket.data.mapper.toCompanyListing
import com.example.stockmarket.data.mapper.toCompanyListingEntity
import com.example.stockmarket.data.remote.StockApi
import com.example.stockmarket.domain.model.CompanyListing
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
    private val companyListingsParser: CSVParser<CompanyListing>
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
}