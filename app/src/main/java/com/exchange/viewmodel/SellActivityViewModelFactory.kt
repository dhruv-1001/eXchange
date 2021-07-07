package com.exchange.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.exchange.repository.FirebaseRepository

class SellActivityViewModelFactory(
    private val firebaseRepo: FirebaseRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SellActivityViewModel(firebaseRepo) as T
    }

}