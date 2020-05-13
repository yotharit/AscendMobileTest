package com.yotharit.ascendtest.module

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.yotharit.ascendtest.api.ProductApi
import com.yotharit.ascendtest.repository.ProductRepositoryImpl
import com.yotharit.ascendtest.ui.detail.ProductDetailViewModel
import com.yotharit.ascendtest.ui.landing.ProductsViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val API_BASE_URL = "https://ecommerce-product-app.herokuapp.com/"

val productModule = module {
	viewModel {
		ProductsViewModel(
			get(ProductRepositoryImpl::class.java),
			androidApplication().applicationContext
		)
	}
	viewModel {
		ProductDetailViewModel(
			get(ProductRepositoryImpl::class.java),
			androidApplication().applicationContext
		)
	}
}

val networkModule = module {
	single {
		createWebService<ProductApi>(
			okHttpClient = createHttpClient(),
			baseUrl = API_BASE_URL
		)
	}
	single {
		ProductRepositoryImpl(productApi = get())
	}
}

fun createHttpClient(): OkHttpClient {
	val client = OkHttpClient.Builder()
	client.readTimeout(5 * 60, TimeUnit.SECONDS)
	return client.addInterceptor {
		val original = it.request()
		val requestBuilder = original.newBuilder()
		requestBuilder.header("Content-Type", "application/json")
		val request = requestBuilder.method(original.method(), original.body()).build()
		return@addInterceptor it.proceed(request)
	}.build()
}

inline fun <reified T> createWebService(
	okHttpClient: OkHttpClient, baseUrl: String
): T {
	val retrofit = Retrofit.Builder().baseUrl(baseUrl)
		.addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
		.addCallAdapterFactory(CoroutineCallAdapterFactory())
		.client(okHttpClient)
		.build()
	return retrofit.create(T::class.java)
}