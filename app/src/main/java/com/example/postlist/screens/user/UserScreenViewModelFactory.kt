package com.example.postlist.screens.user


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.postlist.data.repository.PostRepository

object UserScreenViewModelFactory {
    fun provideFactory(
        repository: PostRepository,
        userId: Int
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserScreenViewModel(repository, userId) as T
        }
    }
}