package com.yotharit.ascendtest.ui.landing.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yotharit.ascendtest.R
import com.yotharit.ascendtest.base.BaseViewHolder
import com.yotharit.ascendtest.extensions.loadFromUrl
import com.yotharit.ascendtest.extensions.visibleIf
import com.yotharit.ascendtest.extensions.visibleIfNotNull
import com.yotharit.ascendtest.model.ProductResponseItem
import kotlinx.android.synthetic.main.product_list_item.view.*

class ProductsRecyclerAdapter(
	private val productsList: ArrayList<ProductResponseItem>,
	private val onItemClick: (id: Int) -> Unit
) :
	RecyclerView.Adapter<BaseViewHolder<ProductResponseItem>>() {

	companion object {
		const val HEADER = 1
		const val ITEM = 2
	}

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): BaseViewHolder<ProductResponseItem> {
		return when (viewType) {
			HEADER -> {
				ProductsHeaderViewHolder(
					LayoutInflater.from(parent.context)
						.inflate(R.layout.product_header_item, parent, false)
				)
			}
			ITEM -> {
				ProductsViewHolder(
					LayoutInflater.from(parent.context)
						.inflate(R.layout.product_list_item, parent, false)
				)
			}
			else -> {
				ProductsViewHolder(
					LayoutInflater.from(parent.context)
						.inflate(R.layout.product_list_item, parent, false)
				)
			}
		}
	}

	override fun onBindViewHolder(holder: BaseViewHolder<ProductResponseItem>, position: Int) {
		if (!isHeader(position)) {
			holder.bind(productsList[position - 1])
			holder.itemView.setOnClickListener {
				onItemClick.invoke(productsList[position - 1].id)
			}
		}
	}

	override fun getItemCount(): Int {
		return productsList.size + 1
	}

	override fun getItemViewType(position: Int): Int {
		return if (isHeader(position)) HEADER else ITEM
	}

	fun setData(updateList: ArrayList<ProductResponseItem>) {
		productsList.clear()
		productsList.addAll(updateList)
		notifyDataSetChanged()
	}

	private fun isHeader(position: Int): Boolean {
		return position == 0
	}

	inner class ProductsViewHolder(itemView: View) : BaseViewHolder<ProductResponseItem>(itemView) {
		override fun bind(item: ProductResponseItem) {
			itemView.productImageView.loadFromUrl(item.image)
			itemView.productNameTextView.text = item.title
			itemView.newProductTextView.visibleIf(item.isNewProduct)
			itemView.productPriceTextView.visibleIfNotNull(item.price) {
				text = item.price.toString()
			}
		}
	}

	inner class ProductsHeaderViewHolder(itemView: View) :
		BaseViewHolder<ProductResponseItem>(itemView) {
		override fun bind(item: ProductResponseItem) {}
	}


}