package com.example.postlist.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postlist.data.model.ToDo
import com.example.postlist.data.model.User
import com.example.postlist.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserScreenViewModel(
    private val repository: PostRepository,
    private val userId: Int
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserDetailsUiState())
    val uiState: StateFlow<UserDetailsUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    fun loadUserData() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val user = repository.getUser(userId)
                val todos = repository.getUserTodos(userId)
                _uiState.update {
                    it.copy(
                        user = user,
                        todos = todos,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.localizedMessage ?: "Failed to load user data"
                    )
                }
            }
        }
    }
}

data class UserDetailsUiState(
    val user: User? = null,
    val todos: List<ToDo> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)