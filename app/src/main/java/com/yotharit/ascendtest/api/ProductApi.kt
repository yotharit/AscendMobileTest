package com.yotharit.ascendtest.api

import com.yotharit.ascendtest.model.ProductResponse
import com.yotharit.ascendtest.model.ProductResponseItem
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {
    @GET("products")
    fun getProductsAsync() : Deferred<ProductResponse>

    @GET("products/{productId}")
    fun getProductByIdAsync(@Path("productId") productId: Int) : Deferred<ProductResponseItem>
}