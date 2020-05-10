package com.yotharit.ascendtest.extensions

interface Otherwise {
	fun otherwise(func: () -> Unit)
}

object OtherwiseInvoke : Otherwise {
	override fun otherwise(func: () -> Unit) {
		func()
	}
}

object OtherwiseIgnore : Otherwise {
	override fun otherwise(func: () -> Unit) {
	}
}

interface OtherwiseWhenValue<T> {
	fun otherwise(func: (T) -> Unit)
}

class OtherwiseWhenValueInvoke<T>(val value: T) : OtherwiseWhenValue<T> {
	override fun otherwise(func: (T) -> Unit) {
		func(value)
	}
}

class OtherwiseWhenValueIgnore<T> : OtherwiseWhenValue<T> {
	override fun otherwise(func: (T) -> Unit) {
	}
}
