package com.exchange.firebaseservice

import com.google.firebase.storage.FirebaseStorage

class FirebaseCalls {

    val imageReference = FirebaseStorage.getInstance().reference.child("images")

    suspend fun uploadImagesToFirebase(): String{
        return ""
    }

}