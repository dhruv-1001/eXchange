package com.exchange.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.exchange.R
import com.exchange.databinding.ActivitySellBinding
import com.exchange.viewmodel.SellActivityViewModel


class SellActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySellBinding
    val viewModel: SellActivityViewModel by viewModels()

    val PICK_IMAGE_MULTIPLE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sell)
        supportActionBar?.hide()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
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

    }

    private var galleryResultActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
            Log.d("Intent Data", "Fetched")
            viewModel.updateSelectedImages(data)
//            for (i in 0 until data!!.clipData!!.itemCount){
//                Log.d("Data", data.clipData!!.getItemAt(i).uri.toString())
//            }
        }
    }

    private fun fetchImages(){

        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        galleryIntent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_IMAGE_MULTIPLE)
        galleryResultActivity.launch(galleryIntent)

    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && data != null){
//            viewModel.updateSelectedImages(data.clipData)
//        }
//
//    }

}