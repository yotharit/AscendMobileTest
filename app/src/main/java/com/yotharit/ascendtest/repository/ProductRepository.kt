package com.yotharit.ascendtest.repository

import com.yotharit.ascendtest.model.ProductResponse
import com.yotharit.ascendtest.model.ProductResponseItem
import com.yotharit.ascendtest.utils.UseCaseResult

interface ProductRepository {
    suspend fun getProducts() : UseCaseResult<ProductResponse>
    suspend fun getProductById(productId: Int) : UseCaseResult<ProductResponseItem>
}