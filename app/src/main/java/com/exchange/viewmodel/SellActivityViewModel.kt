package com.exchange.viewmodel

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.collections.ArrayList


class SellActivityViewModel: ViewModel() {

    // All-----------------------------

    var viewImageVisible = MutableLiveData(false)
    var viewDetailsVisible = MutableLiveData(true)
    var viewSelectedImagesVisible = MutableLiveData(false)
    var viewSetLocationVisible = MutableLiveData(false)

    private var stage = 1

    // Changes stage
    // 1-> Details visible
    // 2-> Select Images/ Selected Images visible
    // 3-> Set Location visible
    fun stageChange(toAdd: Int){
        stage += toAdd
        if (stage == 4) {
            stage = 3
            return
        }
        updateVisibleView(stage)
    }

    // Changes visibility of linear layouts according to the present stage
    private fun updateVisibleView(stage: Int){
        if (stage == 1){
            viewImageVisible.value = false
            viewDetailsVisible.value = true
        } else if (stage == 2){
            viewDetailsVisible.value = false
            viewSetLocationVisible.value = false
            viewImageVisible.value = true
        }
        else{
            viewImageVisible.value = false
            viewSetLocationVisible.value = true
        }
    }

    // Details----------------------------

    var finishActivity = MutableLiveData(false)

    // Observed in SellActivity.kt to finish the activity
    fun finishSellActivity(){
        finishActivity.value = true
    }


    // Images-----------------------------

    private var totalSelectedImages: Int = 0
    private var selectedImageIndex: Int = -1
    var selectedImagesText = MutableLiveData("Images Selected : 0")
    var selectedImageUri = MutableLiveData(Uri.EMPTY)
    private var imagesUri: MutableList<Uri> = ArrayList()

    // Called from SellActivity.kt on getting results from galleryIntent
    // Updates imageUri(ArrayList) with the result
    fun updateSelectedImages(data: Intent?) {
        viewSelectedImagesVisible.value = true
        imagesUri.clear()
        for (i in 0 until data!!.clipData!!.itemCount){
            imagesUri.add(data.clipData!!.getItemAt(i).uri)
        }
        selectedImageIndex = 0
        changeImage()
        totalSelectedImages = imagesUri.size
        selectedImagesText.value = "Images Selected : $totalSelectedImages"
    }

    // Changes visible image Uri
    private fun changeImage(){
        selectedImageUri.value = imagesUri[selectedImageIndex]
    }

    // Changed index of visible image's Uri in imagesUri
    fun changeSelectedImageIndex(toAdd: Int){
        selectedImageIndex += toAdd
        if (selectedImageIndex == imagesUri.size) selectedImageIndex = 0
        if (selectedImageIndex == -1) selectedImageIndex = imagesUri.size - 1
        changeImage()
    }

    // galleryIntent observed in SellActivity.kt
    // opens gallery intent for result if galleryIntent == true
    var galleryIntent = MutableLiveData(false)
    fun getGallery(){
        galleryIntent.value = true
    }

    //Location-----------------------------

    var locationIntent = MutableLiveData(false)
    fun getLocation(){
        locationIntent.value = true
    }

}