package com.example.qodem.ui.settingsandoptions

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.qodem.R
import com.example.qodem.databinding.FragmentSettingsBinding
import com.example.qodem.ui.authentication.AuthenticationActivity
import com.firebase.ui.auth.AuthUI
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSettingsBinding.bind(view)

        binding.apply {
            imageEditUserInfo.setOnClickListener {
                val action = SettingsFragmentDirections.actionSettingsFragmentToUserInfoFragment()
                findNavController().navigate(action)
            }
            cardEditUserInfo.setOnClickListener {
                val action = SettingsFragmentDirections.actionSettingsFragmentToUserInfoFragment()
                findNavController().navigate(action)
            }
            imageEditLanguage.setOnClickListener {
                val action = SettingsFragmentDirections.actionSettingsFragmentToLanguageFragment()
                findNavController().navigate(action)
            }
            cardLanguage.setOnClickListener {
                val action = SettingsFragmentDirections.actionSettingsFragmentToLanguageFragment()
                findNavController().navigate(action)
            }
            imageEditNotification.setOnClickListener {
                val action =
                    SettingsFragmentDirections.actionSettingsFragmentToNotificationFragment()
                findNavController().navigate(action)
            }
            cardNotification.setOnClickListener {
                val action =
                    SettingsFragmentDirections.actionSettingsFragmentToNotificationFragment()
                findNavController().navigate(action)
            }
            buttonSignOut.setOnClickListener {
                AuthUI.getInstance()
                    .signOut(requireContext())
                    .addOnCompleteListener { // user is now signed out
                        startActivity(Intent(requireContext(), AuthenticationActivity::class.java))
                    }
            }
        }
    }
}