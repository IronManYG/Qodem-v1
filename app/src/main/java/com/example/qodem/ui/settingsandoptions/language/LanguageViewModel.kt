package com.example.qodem.ui.settingsandoptions.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qodem.data.Language
import com.example.qodem.data.PreferencesManager
import com.zeugmasolutions.localehelper.Locales
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class LanguageViewModel
@Inject
constructor(
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    private val preferencesFlow = preferencesManager.preferencesFlow

    private val languageFlow = preferencesFlow.map {
        it.language
    }
    val language: StateFlow<Language> = languageFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            if (Locale.getDefault() == Locales.English) {
                Language.English
            } else {
                Language.Arabic
            }
        )

    fun onLanguageSelected(Language: Language) = viewModelScope.launch {
        preferencesManager.updateLanguage(Language)
    }
}