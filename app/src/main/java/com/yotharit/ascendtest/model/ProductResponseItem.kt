package com.yotharit.ascendtest.model

data class ProductResponseItem(
    val content: String,
    val id: Int,
    val image: String,
    val isNewProduct: Boolean,
    val title: String,
    val price: Double?
)