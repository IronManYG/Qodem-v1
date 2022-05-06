package com.example.qodem.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PreferencesManager"

data class FilterPreferences(val language: Language)

enum class Language { English, Arabic }

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")


@Singleton
class
PreferencesManager
@Inject
constructor(@ApplicationContext context: Context) {

    private val dataStore = context.dataStore

    val preferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences: ", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val language = Language.valueOf(
                preference[PreferenceKeys.LANGUAGE] ?: Language.Arabic.name
            )
            FilterPreferences(language)
        }

    suspend fun updateLanguage(language: Language) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.LANGUAGE] = language.name
        }
    }

    private object PreferenceKeys {
        val LANGUAGE = stringPreferencesKey("language")
    }
}