package com.example.githubuser.adapter.ui.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetailViewModelFactory (private val mApplication: Application, private val username: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(mApplication, username) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class class: ${modelClass.name}")
    }
}