package com.yotharit.ascendtest.ui.landing

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yotharit.ascendtest.model.ProductResponseItem
import com.yotharit.ascendtest.repository.ProductRepository
import com.yotharit.ascendtest.utils.SingleLiveEvent
import com.yotharit.ascendtest.utils.UseCaseResult
import com.yotharit.ascendtest.utils.network.base.ConnectivityProvider
import com.yotharit.ascendtest.utils.network.base.ConnectivityProvider.ConnectivityStateListener
import com.yotharit.ascendtest.utils.network.base.ConnectivityProvider.NetworkState
import com.yotharit.ascendtest.utils.network.base.ConnectivityProvider.NetworkState.ConnectedState
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ProductsViewModel(private val productRepository: ProductRepository, context: Context) :
	ViewModel(),
	CoroutineScope, ConnectivityStateListener {

	private val job = Job()

	private val provider: ConnectivityProvider by lazy { ConnectivityProvider.createProvider(context) }

	val loading = MutableLiveData<Boolean>()
	val productsList = MutableLiveData<ArrayList<ProductResponseItem>>()
	val showMessage = SingleLiveEvent<String>()
	val product = MutableLiveData<ProductResponseItem>()
	val productLoading = MutableLiveData<Boolean>()
	val itemId = SingleLiveEvent<Int>()

	override val coroutineContext: CoroutineContext = Dispatchers.Main + job

	override fun onCleared() {
		super.onCleared()
		provider.removeListener(this)
		job.cancel()
	}

	override fun onStateChange(state: NetworkState) {
		if (state.hasInternet()) {
			showMessage.value = "Connected to Internet."
		} else {
			showMessage.value = "Network connection lost."
		}
	}

	init {
		fetchProducts()
		provider.addListener(this)
	}

	fun fetchProducts() {
		if (provider.getNetworkState().hasInternet()) {
			loading.value = true
			launch {
				val result = withContext(Dispatchers.IO) { productRepository.getProducts() }
				loading.value = false
				when (result) {
					is UseCaseResult.Success -> {
						productsList.value = result.data
					}
					is UseCaseResult.Error -> {
						showMessage.value = result.exception.message
					}
				}
			}
		} else {
			loading.value = false
			showMessage.value = "Network connection lost."
		}
	}

	fun getProductById(id: Int) {
		if (provider.getNetworkState().hasInternet()) {
			productLoading.value = true
			launch {
				val result = withContext(Dispatchers.IO) { productRepository.getProductById(id) }
				productLoading.value = false
				when (result) {
					is UseCaseResult.Success -> {
						product.value = result.data
					}
					is UseCaseResult.Error -> {
						showMessage.value = result.exception.message
					}
				}
			}
		} else {
			loading.value = false
			showMessage.value = "Network connection lost."
		}
	}

	fun itemClick(id: Int) {
		itemId.value = id
	}

	private fun NetworkState.hasInternet(): Boolean {
		return (this as? ConnectedState)?.hasInternet == true
	}
}