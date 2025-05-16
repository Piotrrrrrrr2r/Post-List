package com.example.postlist.screens.postdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.postlist.data.repository.PostRepository

object DetailsScreenViewModelFactory {
    fun provideFactory(
        repository: PostRepository,
        postId: Int
    ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetailsScreenViewModel(repository, postId) as T
        }
    }
}