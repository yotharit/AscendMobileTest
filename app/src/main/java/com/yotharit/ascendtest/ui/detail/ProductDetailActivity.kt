package com.yotharit.ascendtest.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.yotharit.ascendtest.R
import com.yotharit.ascendtest.extensions.loadFromUrl
import com.yotharit.ascendtest.extensions.visibleIf
import com.yotharit.ascendtest.extensions.visibleIfNotBlank
import com.yotharit.ascendtest.extensions.visibleIfNotNull
import com.yotharit.ascendtest.model.ProductResponseItem
import com.yotharit.ascendtest.ui.landing.ProductsViewModel
import kotlinx.android.synthetic.main.activity_detail_layout.*
import org.koin.android.viewmodel.ext.android.viewModel

class ProductDetailActivity : AppCompatActivity() {

	private val productsViewModel: ProductsViewModel by viewModel(ProductsViewModel::class)
	private var id = -1

	companion object {
		private const val PRODUCT_ID = "id"
		fun createIntent(context: Context, id: Int): Intent {
			return Intent(context, ProductDetailActivity::class.java).putExtra(PRODUCT_ID, id)
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_detail_layout)
		id = intent.getIntExtra(PRODUCT_ID, -1)
		setUpView()
		initViewHolder()
		productsViewModel.getProductById(id)
	}

	private fun setUpView() {
		swipeLayout.setOnRefreshListener(SwipeListener())
	}

	private fun fillData(data: ProductResponseItem) {
		headerTextView.visibility = View.VISIBLE
		productImageView.visibleIfNotBlank(data.image) {
			loadFromUrl(data.image)
		}
		productPriceTextView.visibleIfNotNull(data.price) {
			text = data.price.toString()
		}
		newProductTextView.visibleIf(data.isNewProduct)
		productContentTextView.visibleIfNotBlank(data.content) {
			text = data.content
		}
		productNameTextView.visibleIfNotBlank(data.title) {
			text = data.title
		}
	}

	private fun initViewHolder() {
		productsViewModel.product.observe(this, Observer {
			fillData(it)
		})
		productsViewModel.productLoading.observe(this, Observer {
			swipeLayout.isRefreshing = it
		})
		productsViewModel.showMessage.observe(this, Observer {
			Toast.makeText(this, it, Toast.LENGTH_LONG).show()
		})
	}

	private inner class SwipeListener : SwipeRefreshLayout.OnRefreshListener {
		override fun onRefresh() {
			productsViewModel.getProductById(id)
		}
	}
}