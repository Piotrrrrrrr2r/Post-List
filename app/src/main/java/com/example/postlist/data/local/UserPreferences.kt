package com.example.postlist.data.local
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.userPreferencesStore: DataStore<Preferences> by preferencesDataStore(
    name = "user_preferences"
)

class UserPreferences(context: Context) {
    private val dataStore = context.userPreferencesStore

    companion object {
        val FIRST_NAME = stringPreferencesKey("first_name")
        val LAST_NAME = stringPreferencesKey("last_name")
        val PROFILE_IMAGE_PATH = stringPreferencesKey("profile_image_path")
    }

    val firstName: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[FIRST_NAME] ?: ""
        }

    val lastName: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[LAST_NAME] ?: ""
        }

    val profileImagePath: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[PROFILE_IMAGE_PATH] ?: ""
        }

    suspend fun saveFirstName(firstName: String) {
        dataStore.edit { preferences ->
            preferences[FIRST_NAME] = firstName
        }
    }

    suspend fun saveLastName(lastName: String) {
        dataStore.edit { preferences ->
            preferences[LAST_NAME] = lastName
        }
    }

    suspend fun saveProfileImagePath(path: String) {
        dataStore.edit { preferences ->
            preferences[PROFILE_IMAGE_PATH] = path
        }
    }
}