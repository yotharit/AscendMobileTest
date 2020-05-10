package com.yotharit.ascendtest.repository

import com.yotharit.ascendtest.api.ProductApi
import com.yotharit.ascendtest.model.ProductResponse
import com.yotharit.ascendtest.model.ProductResponseItem
import com.yotharit.ascendtest.utils.UseCaseResult

class ProductRepositoryImpl(private val productApi: ProductApi) : ProductRepository {

    override suspend fun getProducts(): UseCaseResult<ProductResponse> {
        return try {
            val result = productApi.getProductsAsync().await()
            UseCaseResult.Success(result)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }

    override suspend fun getProductById(productId: Int): UseCaseResult<ProductResponseItem> {
        return try {
            val result = productApi.getProductByIdAsync(productId).await()
            UseCaseResult.Success(result)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }
}