package com.exchange.repository

import com.exchange.firebaseservice.FirebaseCalls
import com.exchange.model.Advertisement

class FirebaseRepository {

    private val firebaseCallInstance = FirebaseCalls()

    suspend fun uploadDataToFirebase(data: Advertisement): String{
        return firebaseCallInstance.uploadImagesToFirebase()
    }

}