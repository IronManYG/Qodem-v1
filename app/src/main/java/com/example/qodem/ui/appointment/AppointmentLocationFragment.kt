package com.example.qodem.ui.appointment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qodem.R
import com.example.qodem.databinding.FragmentAppointmentLocationBinding
import com.example.qodem.databinding.FragmentPreScreeningQuestionBinding
import com.example.qodem.ui.BloodBankAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AppointmentLocationFragment : Fragment() {

    companion object {
        const val TAG = "AppointmentLocationFragment"
    }

    private val viewModel: AppointmentLocationViewModel by viewModels()

    private lateinit var binding: FragmentAppointmentLocationBinding

    //
    private lateinit var bloodBankAdapter: BloodBankAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAppointmentLocationBinding.inflate(layoutInflater)

        //
        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.bloodBanksList.observe(viewLifecycleOwner,{ bloodBanks ->
            //
            bloodBankAdapter.bloodBanks = bloodBanks
        })
    }

    //
    private fun setupRecyclerView() = binding.recyclerViewBloodBanks.apply {
        bloodBankAdapter = BloodBankAdapter()
        adapter = bloodBankAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

}