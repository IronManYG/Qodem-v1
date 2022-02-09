package com.example.qodem.ui.appointment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.qodem.R
import com.example.qodem.databinding.FragmentPreScreeningRequestBinding
import com.example.qodem.ui.bloodbanklocation.LocationFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class PreScreeningRequestFragment : Fragment() {

    private lateinit var binding: FragmentPreScreeningRequestBinding

    private val args: PreScreeningRequestFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPreScreeningRequestBinding.inflate(layoutInflater)

        binding.buttonProceedNow.setOnClickListener {
            val amount = args.bloodBankID
            val action = PreScreeningRequestFragmentDirections.actionPreScreeningRequestFragmentToPreScreeningQuestion(amount)
            findNavController().navigate(action)
        }

        return binding.root
    }
}