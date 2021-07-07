package com.exchange.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.exchange.R
import com.exchange.databinding.ActivitySellBinding
import com.exchange.viewmodel.SellActivityViewModel
import com.google.android.material.snackbar.Snackbar


class SellActivity() : AppCompatActivity() {

    private lateinit var binding: ActivitySellBinding
    private val viewModel: SellActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sell)
        supportActionBar?.hide()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.missingData.observe(this,{
            Snackbar.make(findViewById(android.R.id.content), viewModel.errorMessage.value.toString(), Snackbar.LENGTH_LONG).show()
        })

    }

    override fun onResume() {
        super.onResume()

        viewModel.galleryIntent.observe(this, {
            if (it == true) {
                viewModel.galleryIntent.value = false
                fetchImages()
            }
        })

        viewModel.finishActivity.observe(this, {
            if (it == true){
                viewModel.finishActivity.value = false
                this.finish()
            }
        })

        viewModel.locationIntent.observe(this, {
            if (it == true){
                viewModel.locationIntent.value = false
                fetchLocation()
            }
        })



    }

    private var galleryResultActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
            viewModel.updateSelectedImages(data)
        }
    }

    private fun fetchImages(){

        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryResultActivity.launch(galleryIntent)

    }

    private var locationResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
        }
    }

    private fun fetchLocation(){
        val locationIntent = Intent()
        locationIntent.action = Intent.ACTION_VIEW
//        val gmmIntentUri = Uri.parse("geo:37.7749,-122.4194")
//        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//        mapIntent.setPackage("com.google.android.apps.maps")
//        startActivity(mapIntent)
    }

}