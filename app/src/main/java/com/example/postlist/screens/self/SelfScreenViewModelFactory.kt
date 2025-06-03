package com.example.postlist.screens.self

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.postlist.data.local.UserPreferences

class SelfScreenViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SelfScreenViewModel::class.java)) {
            val userPreferences = UserPreferences(context)
            @Suppress("UNCHECKED_CAST")
            return SelfScreenViewModel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        val factory: (Context) -> SelfScreenViewModelFactory = { context ->
            SelfScreenViewModelFactory(context)
        }
    }
}