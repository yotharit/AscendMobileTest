package com.yotharit.ascendtest.ui.landing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.yotharit.ascendtest.R
import com.yotharit.ascendtest.ui.detail.ProductDetailActivity
import com.yotharit.ascendtest.ui.landing.adapter.ProductsRecyclerAdapter
import com.yotharit.ascendtest.utils.GridItemDecoration
import com.yotharit.ascendtest.utils.dpToPixel
import kotlinx.android.synthetic.main.products_fragment_layout.*
import org.koin.android.viewmodel.ext.android.viewModel

class ProductsActivityFragment : Fragment() {

	private val productsViewModel: ProductsViewModel by viewModel(ProductsViewModel::class)
	private lateinit var adapter: ProductsRecyclerAdapter

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.products_fragment_layout, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpView()
		initViewModel()
	}

	private fun setUpView() {
		val onItemClicked: (id: Int) -> Unit = { id ->
			productsViewModel.itemClick(id)
		}
		adapter = ProductsRecyclerAdapter(arrayListOf(), onItemClicked)
		recyclerView.adapter = adapter
		val layoutManager = GridLayoutManager(context, 2)
		layoutManager.spanSizeLookup = ProductGridSpanLookup()
		recyclerView.layoutManager = layoutManager
		swipeLayout.setOnRefreshListener(SwipeListener())
		recyclerView.addItemDecoration(GridItemDecoration(dpToPixel(context, 15)))
	}

	private fun initViewModel() {
		productsViewModel.productsList.observe(this, Observer {
			adapter.setData(it)
		})
		productsViewModel.showError.observe(this, Observer {
			Toast.makeText(context, it, Toast.LENGTH_LONG).show()
			Log.d("error-vm", it)
		})
		productsViewModel.loading.observe(this, Observer {
			swipeLayout.isRefreshing = it
		})
		productsViewModel.itemId.observe(this, Observer { id ->
			context?.let {
				startActivity(ProductDetailActivity.createIntent(it, id))
			}
		})
	}

	private inner class ProductGridSpanLookup : GridLayoutManager.SpanSizeLookup() {
		override fun getSpanSize(position: Int): Int {
			return when (adapter.getItemViewType(position)) {
				ProductsRecyclerAdapter.HEADER -> 2
				ProductsRecyclerAdapter.ITEM -> 1
				else -> -1
			}
		}
	}

	private inner class SwipeListener : SwipeRefreshLayout.OnRefreshListener {
		override fun onRefresh() {
			productsViewModel.fetchProducts()
		}
	}

}