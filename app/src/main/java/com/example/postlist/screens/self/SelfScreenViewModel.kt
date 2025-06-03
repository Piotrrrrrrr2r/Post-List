package com.example.postlist.screens.self

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postlist.data.local.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class SelfScreenViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {
    var firstName by mutableStateOf("")
    // Remove private set to make it publicly settable
    // set // This line should be removed or commented out

    var lastName by mutableStateOf("")
    // Remove private set to make it publicly settable
    // set // This line should be removed or commented out

    var profileImageUri by mutableStateOf<Uri?>(null)
    // Remove private set to make it publicly settable
    // set // This line should be removed or commented out

    var profileImagePath by mutableStateOf<String?>(null)
        private set // Keep this private as it's only set internally

    init {
        viewModelScope.launch {
            // Load saved data
            firstName = userPreferences.firstName.first()
            lastName = userPreferences.lastName.first()
            profileImagePath = userPreferences.profileImagePath.first()
        }
    }

    fun saveUserDetails(context: Context) {
        viewModelScope.launch {
            // Save text data
            userPreferences.saveFirstName(firstName)
            userPreferences.saveLastName(lastName)

            // Save image if selected
            profileImageUri?.let { uri ->
                val file = saveImageToInternalStorage(context, uri)
                profileImagePath = file?.absolutePath
                file?.absolutePath?.let { path ->
                    userPreferences.saveProfileImagePath(path)
                }
            }
        }
    }

    private fun saveImageToInternalStorage(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileName = "profile_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)

            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}