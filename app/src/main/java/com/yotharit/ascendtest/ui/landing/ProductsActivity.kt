package com.yotharit.ascendtest.ui.landing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yotharit.ascendtest.R
import kotlinx.android.synthetic.main.activity_layout.*

class ProductsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(fragmentContainer.id, ProductsActivityFragment.newInstance(), null).commit()
        }
    }

}
