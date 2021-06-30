package com.exchange.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SellActivityViewModel: ViewModel() {


    var viewImageVisible = MutableLiveData(false)
    var viewDetailsVisible = MutableLiveData(true)
    var viewSelectedImagesVisible = MutableLiveData(false)

    var totalSelectedImages = MutableLiveData(0)
    var selectedImagesText = MutableLiveData("Images Selected : 0")

    private var stage = 1

    private fun updateView(stage: Int){
        if (stage == 1){
            viewImageVisible.value = false
            viewDetailsVisible.value = true
        } else if (stage == 2){
            viewDetailsVisible.value = false
            viewImageVisible.value = true
        }
    }

    fun stageUp(){
        stage++
        if (stage == 3) stage = 1
        updateView(stage)
    }

}