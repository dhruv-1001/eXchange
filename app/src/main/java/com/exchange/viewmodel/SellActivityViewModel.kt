package com.exchange.viewmodel

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exchange.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.text.DateFormat.getDateTimeInstance
import java.util.*
import kotlin.collections.ArrayList


class SellActivityViewModel(
    private val firebaseRepo: FirebaseRepository
): ViewModel() {

    // All-----------------------------


    var viewImageVisible = MutableLiveData(false)
    var viewDetailsVisible = MutableLiveData(true)
    var viewSelectedImagesVisible = MutableLiveData(false)
    var viewSetLocationVisible = MutableLiveData(false)
    var errorMessage = MutableLiveData("")

    private var stage = 1

    private var _missingData = MutableLiveData<Boolean>()
    val missingData: LiveData<Boolean?>  get() = _missingData

    // Changes stage
    // 1-> Details visible
    // 2-> Select Images/ Selected Images visible
    // 3-> Set Location visible
    fun stageChange(toAdd: Int){

        if (stage == 1){
            if (!checkInputFields()) {
                updateErrorMessage("Please enter all the fields")
                return
            }
        }
        if (stage == 2){
            if (!checkImages()) {
                updateErrorMessage("Please select at least 3 images")
                return
            }
        }
        stage += toAdd
        if (stage == 4) {
            uploadProductData()
            stage = 3
            return
        }
        updateVisibleView(stage)
    }

    private fun updateErrorMessage(errorMsg: String){
        errorMessage.value = errorMsg
        _missingData.value = _missingData.value != true
    }

    // Changes visibility of linear layouts according to the present stage
    private fun updateVisibleView(stage: Int){
        when (stage) {
            1 -> {
                viewImageVisible.value = false
                viewDetailsVisible.value = true
            }
            2 -> {
                viewDetailsVisible.value = false
                viewSetLocationVisible.value = false
                viewImageVisible.value = true
            }
            else -> {
                viewImageVisible.value = false
                viewSetLocationVisible.value = true
            }
        }
    }

    // Details----------------------------

    var editTextProductName = MutableLiveData("")
    var editTextProductType = MutableLiveData("")
    var editTextProductDescription = MutableLiveData("")
    var editTextProductPrice = MutableLiveData("")

    private fun checkInputFields(): Boolean{
        if (editTextProductName.value?.isEmpty() == true) return false
        if (editTextProductType.value?.isEmpty() == true) return false
        if (editTextProductDescription.value?.isEmpty() == true) return false
        if (editTextProductPrice.value?.isEmpty() == true) return false
        return true
    }

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

    private fun checkImages(): Boolean{
        if (imagesUri.size <= 2) return false
        return true
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

    // Firebase----------------------------

    private val auth = FirebaseAuth.getInstance()
    private val imageStorageReference = FirebaseStorage.getInstance().reference.child("images")
    private val downloadUrlList: MutableList<String> = ArrayList()

    private fun uploadText(): Boolean{
        return false
    }

    private fun uploadImages(imageIndex: Int): Boolean{
        if (imageIndex == imagesUri.size) return true
        var toReturn = true
        val fileName: String = auth.currentUser?.uid + "_" + getDateTimeInstance().format(Date()) + imageIndex.toString()
        val fileReference = imageStorageReference.child(fileName)
        fileReference
            .putFile(imagesUri[imageIndex])
            .addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener {
                    downloadUrlList.add(it.toString())
                    uploadImages(imageIndex+1)
                }
                .addOnFailureListener {
                    updateErrorMessage(it.toString())
                    toReturn = false
                }
            }
            .addOnFailureListener{
                updateErrorMessage("Problem uploading images. Please your internet and try again.")
                toReturn = false
            }
        return toReturn
    }

    private fun uploadProductData(){
//        if (uploadImages(0) && uploadText()){
//            updateErrorMessage("Success")
//        }
//        else{
//            updateErrorMessage("Problem uploading images. Check your connection and try again.")
//        }
    }

}