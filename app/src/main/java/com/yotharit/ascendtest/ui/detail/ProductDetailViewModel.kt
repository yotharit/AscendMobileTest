package com.yotharit.ascendtest.ui.detail

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yotharit.ascendtest.model.ProductResponseItem
import com.yotharit.ascendtest.repository.ProductRepository
import com.yotharit.ascendtest.utils.SingleLiveEvent
import com.yotharit.ascendtest.utils.UseCaseResult
import com.yotharit.ascendtest.utils.network.base.ConnectivityProvider
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ProductDetailViewModel(private val productRepository: ProductRepository, context: Context) :
	ViewModel(),
	CoroutineScope, ConnectivityProvider.ConnectivityStateListener {

	private val job = Job()

	private val provider: ConnectivityProvider by lazy { ConnectivityProvider.createProvider(context) }
	val showMessage = SingleLiveEvent<String>()
	val product = MutableLiveData<ProductResponseItem>()
	val productLoading = MutableLiveData<Boolean>()

	override val coroutineContext: CoroutineContext = Dispatchers.Main + job

	override fun onCleared() {
		super.onCleared()
		provider.removeListener(this)
		job.cancel()
	}

	override fun onStateChange(state: ConnectivityProvider.NetworkState) {
		if (state.hasInternet()) {
			showMessage.value = "Connected to Internet."
		} else {
			showMessage.value = "Network connection lost."
		}
	}

	init {
		provider.addListener(this)
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
			productLoading.value = false
			showMessage.value = "Network connection lost."
		}
	}

	private fun ConnectivityProvider.NetworkState.hasInternet(): Boolean {
		return (this as? ConnectivityProvider.NetworkState.ConnectedState)?.hasInternet == true
	}
}