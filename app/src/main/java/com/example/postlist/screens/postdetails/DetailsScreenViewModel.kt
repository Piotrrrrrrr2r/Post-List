package com.example.postlist.screens.postdetails
import androidx.lifecycle.*
import com.example.postlist.data.model.Post
import com.example.postlist.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsScreenViewModel(
    private val repository: PostRepository,
    private val postId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState

    init {
        loadPostDetails()
    }

    fun loadPostDetails() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val post = repository.getPost(postId)
                _uiState.update {
                    it.copy(
                        post = post,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.localizedMessage ?: "Unknown error occurred"
                    )
                }
            }
        }
    }
}

data class DetailsUiState(
    val post: Post? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)