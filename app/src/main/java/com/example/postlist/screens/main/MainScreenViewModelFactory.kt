package com.example.postlist.screens.main

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.postlist.data.api.ApiClient
import com.example.postlist.data.repository.PostRepositoryImpl

object MainScreenViewModelFactory {
    val factory = viewModelFactory {
        initializer {
            MainScreenViewModel(
                repository = PostRepositoryImpl(ApiClient.instance)
            )
        }
    }
}