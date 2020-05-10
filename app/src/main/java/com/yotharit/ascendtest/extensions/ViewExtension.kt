package com.yotharit.ascendtest.extensions

import android.view.View

fun <T : View, U> T.visibleIfNotNull(
	checkNullData: U?, notVisibleState: Int = View.GONE, whenVisible: (T.(U) -> Unit)? = null
): OtherwiseWhenValue<T> {

	return if (checkNullData != null) {
		whenVisible?.invoke(this, checkNullData)
		this.visibility = View.VISIBLE
		OtherwiseWhenValueIgnore()
	} else {
		this.visibility = notVisibleState
		OtherwiseWhenValueInvoke(this)
	}
}

fun <T : View, U : CharSequence> T.visibleIfNotBlank(
	text: U?, notVisibleState: Int = View.GONE, whenVisible: (T.(U) -> Unit)? = null
): OtherwiseWhenValue<T> {

	return if (text != null && text.isNotBlank()) {
		whenVisible?.invoke(this, text)
		this.visibility = View.VISIBLE
		OtherwiseWhenValueIgnore()
	} else {
		this.visibility = notVisibleState
		OtherwiseWhenValueInvoke(this)
	}
}

fun <T : View, U : Collection<Any>> T.visibleIfNotEmpty(
	collection: U?, notVisibleState: Int = View.GONE, whenVisible: (T.(U) -> Unit)? = null
): OtherwiseWhenValue<T> {

	return if (collection != null && collection.isNotEmpty()) {
		whenVisible?.invoke(this, collection)
		this.visibility = View.VISIBLE
		OtherwiseWhenValueIgnore()
	} else {
		this.visibility = notVisibleState
		OtherwiseWhenValueInvoke(this)
	}
}

fun <T : View> T.visibleIfNotEmpty(
	array: Array<*>?, notVisibleState: Int = View.GONE, whenVisible: (T.(Array<*>) -> Unit)? = null
): OtherwiseWhenValue<T> {

	return if (array != null && array.isNotEmpty()) {
		whenVisible?.invoke(this, array)
		this.visibility = View.VISIBLE
		OtherwiseWhenValueIgnore()
	} else {
		this.visibility = notVisibleState
		OtherwiseWhenValueInvoke(this)
	}
}

fun <T : View> T.visibleIf(
	isVisible: Boolean, notVisibleState: Int = View.GONE, whenVisible: (T.() -> Unit)? = null
): OtherwiseWhenValue<T> {
	return if (isVisible) {
		whenVisible?.invoke(this)
		this.visibility = View.VISIBLE
		OtherwiseWhenValueIgnore()
	} else {
		this.visibility = notVisibleState
		OtherwiseWhenValueInvoke(this)
	}
}