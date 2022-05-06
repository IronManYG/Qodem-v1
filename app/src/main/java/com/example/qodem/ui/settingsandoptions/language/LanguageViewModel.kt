package com.example.qodem.ui.settingsandoptions.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qodem.data.Language
import com.example.qodem.data.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class LanguageViewModel
@Inject
constructor(
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    private val preferencesFlow = preferencesManager.preferencesFlow

    fun onLanguageSelected(Language: Language) = viewModelScope.launch {
        preferencesManager.updateLanguage(Language)
    }
}