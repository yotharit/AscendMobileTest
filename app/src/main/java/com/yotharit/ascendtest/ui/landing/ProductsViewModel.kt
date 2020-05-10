package com.yotharit.ascendtest.ui.landing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yotharit.ascendtest.model.ProductResponseItem
import com.yotharit.ascendtest.repository.ProductRepository
import com.yotharit.ascendtest.utils.SingleLiveEvent
import com.yotharit.ascendtest.utils.UseCaseResult
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ProductsViewModel(private val productRepository: ProductRepository) : ViewModel(),
	CoroutineScope {

	private val job = Job()

	val loading = MutableLiveData<Boolean>()
	val productsList = MutableLiveData<ArrayList<ProductResponseItem>>()
	val showError = SingleLiveEvent<String>()
	val product = MutableLiveData<ProductResponseItem>()
	val productLoading = MutableLiveData<Boolean>()
	val itemId = SingleLiveEvent<Int>()

	override val coroutineContext: CoroutineContext = Dispatchers.Main + job

	override fun onCleared() {
		super.onCleared()
		job.cancel()
	}

	init {
		fetchProducts()
	}

	fun fetchProducts() {
		loading.value = true
		launch {
			val result = withContext(Dispatchers.IO) { productRepository.getProducts() }
			loading.value = false
			when (result) {
				is UseCaseResult.Success -> {
					productsList.value = result.data
				}
				is UseCaseResult.Error -> {
					showError.value = result.exception.message
				}
			}
		}
	}

	fun getProductById(id: Int) {
		productLoading.value = true
		launch {
			val result = withContext(Dispatchers.IO) { productRepository.getProductById(id) }
            productLoading.value = false
            when (result) {
                is UseCaseResult.Success -> {
                    product.value = result.data
                }
                is UseCaseResult.Error -> {
                    showError.value = result.exception.message
                }
            }
		}
	}

	fun itemClick(id: Int) {
		itemId.value = id
	}
}