package com.yotharit.ascendtest.extensions

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun CharSequence?.isSafeNotBlank(): Boolean = this != null && this.isNotBlank()

fun CharSequence?.isSafeNotEmpty(): Boolean = this != null && this.isNotEmpty()

inline fun <T : CharSequence> T?.whenNotBlank(block: (T) -> Unit): Otherwise {
	return if (this != null && this.isSafeNotBlank()) {
		block.invoke(this)
		OtherwiseIgnore
	} else {
		OtherwiseInvoke
	}
}

@JvmOverloads
fun <T> List<T>.toStringWithSeparator(
	max: Int = Int.MAX_VALUE, separator: String = ", ", ellipseEnd: String = "...", processor: (tObject: T) -> String
): String {
	val stringList = this.map(processor)
	return stringList.toStringWithSeparator(max, separator, ellipseEnd)
}

@JvmOverloads
fun List<String>.toStringWithSeparator(
	max: Int = Int.MAX_VALUE, separator: String = ", ", ellipseEnd: String = "..."
): String {

	val builder = StringBuilder()
	this.forEachIndexed { index, item ->
		if (index > max) {
			builder.append(ellipseEnd)
			return@forEachIndexed
		}
		if (builder.isNotEmpty()) {
			builder.append(separator)
		}
		builder.append(item)
	}
	return builder.toString()
}

fun String.isNumeric(): Boolean {
	return this.matches("[-+]?\\d*\\.?\\d+".toRegex())
}

fun String.toDate(dateFormat: SimpleDateFormat): Date? {
	return try {
		dateFormat.parse(this)
	} catch (e: ParseException) {
		null
	}
}