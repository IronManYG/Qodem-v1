package com.example.qodem.ui.appointment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.qodem.R
import com.example.qodem.databinding.FragmentPreScreeningRequestBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class PreScreeningRequestFragment : Fragment(R.layout.fragment_pre_screening_request) {

    private lateinit var binding: FragmentPreScreeningRequestBinding

    private val args: PreScreeningRequestFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inflate the layout for this fragment
        binding = FragmentPreScreeningRequestBinding.bind(view)

        binding.buttonProceedNow.setOnClickListener {
            val action = PreScreeningRequestFragmentDirections.actionPreScreeningRequestFragmentToPreScreeningQuestion(args.bloodBankID, args.selectedBloodBank)
            findNavController().navigate(action)
        }

    }
}