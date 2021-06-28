package com.exchange.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.exchange.R

class SellActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell)
    }

    override fun onStart() {
        super.onStart()
        supportActionBar?.hide()
    }

}