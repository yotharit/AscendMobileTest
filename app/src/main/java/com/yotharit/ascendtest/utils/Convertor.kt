package com.yotharit.ascendtest.utils

import android.content.Context

fun dpToPixel(context: Context?, dp: Int): Int {
	return if (context == null) {
		0
	} else {
		(context.resources.displayMetrics.density * dp).toInt()
	}
}