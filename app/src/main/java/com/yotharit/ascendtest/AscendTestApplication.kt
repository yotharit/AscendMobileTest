package com.yotharit.ascendtest

import android.app.Application
import com.yotharit.ascendtest.module.networkModule
import com.yotharit.ascendtest.module.productModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AscendTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AscendTestApplication)
            modules(listOf(productModule, networkModule))
        }
    }
}