package com.exchange.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.exchange.R
import com.exchange.databinding.ActivitySellBinding
import com.exchange.viewmodel.SellActivityViewModel

class SellActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySellBinding
    val viewModel: SellActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sell)
        supportActionBar?.hide()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()

    }

}