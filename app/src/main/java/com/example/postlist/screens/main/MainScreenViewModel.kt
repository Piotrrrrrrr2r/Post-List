package com.example.postlist.screens.main
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postlist.data.model.PostWithUser
import com.example.postlist.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainScreenViewModel(private val repository: PostRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val data = repository.getPostsWithUsers()
                _uiState.update {
                    it.copy(
                        allPosts = data,
                        filteredPosts = data,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load data: ${e.localizedMessage}"
                    )
                }
            }
        }
    }
}

data class MainScreenUiState(
    val allPosts: List<PostWithUser> = emptyList(),
    val filteredPosts: List<PostWithUser> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    )