package com.example.qodem.ui.settingsandoptions.language

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.qodem.R
import com.example.qodem.data.Language
import com.example.qodem.databinding.FragmentLanguageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LanguageFragment : Fragment(R.layout.fragment_language) {

    private val viewModel: LanguageViewModel by viewModels()

    private lateinit var binding: FragmentLanguageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLanguageBinding.bind(view)

        binding.apply {
            cardArLanguage.setOnClickListener {
                viewModel.onLanguageSelected(Language.Arabic)
            }
            imageEditArLanguage.setOnClickListener {
                viewModel.onLanguageSelected(Language.Arabic)
            }
            cardEnLanguage.setOnClickListener {
                viewModel.onLanguageSelected(Language.English)
            }
            imageEditEnLanguage.setOnClickListener {
                viewModel.onLanguageSelected(Language.English)
            }
        }
    }
}