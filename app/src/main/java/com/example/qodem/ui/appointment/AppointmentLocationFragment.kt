package com.example.qodem.ui.appointment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qodem.R
import com.example.qodem.databinding.FragmentAppointmentLocationBinding
import com.example.qodem.model.BloodBank
import com.example.qodem.ui.BloodBankAdapter
import com.example.qodem.utils.dialIntent
import com.example.qodem.utils.exhaustive
import com.example.qodem.utils.mapIntent
import com.example.qodem.utils.showSnackBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AppointmentLocationFragment : Fragment(R.layout.fragment_appointment_location),
    BloodBankAdapter.OnBloodBankItemClickListener {

    companion object {
        const val TAG = "AppointmentLocationFrag"
    }

    private val viewModel: AppointmentLocationViewModel by viewModels()

    private lateinit var binding: FragmentAppointmentLocationBinding

    private lateinit var bloodBankAdapter: BloodBankAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAppointmentLocationBinding.bind(view)

        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.bloodBanksList.collect { bloodBanks ->
                        bloodBankAdapter.submitList(bloodBanks)
                    }
                }
                @Suppress("IMPLICIT_CAST_TO_ANY")
                launch {
                    viewModel.appointmentLocationsEvents.collect { event ->
                        when (event) {
                            is AppointmentLocationViewModel.AppointmentLocationEvent.BloodBankIsSelected -> {
                                viewModel.onBloodBankSelectedChanged(event.bloodBank)
                            }
                            is AppointmentLocationViewModel.AppointmentLocationEvent.NavigateToDialApp -> {
                                dialIntent(event.bloodBank.phoneNumber, requireContext())
                            }
                            is AppointmentLocationViewModel.AppointmentLocationEvent.NavigateToMapsApp -> {
                                mapIntent(event.bloodBank, requireContext())
                            }
                        }.exhaustive
                    }
                }
            }
        }

        binding.buttonNextStep.setOnClickListener {
            if (viewModel.selectedBloodBank != null) {
                val action =
                    AppointmentLocationFragmentDirections.actionAppointmentLocationFragmentToAppointmentDataFragment(
                        bloodBankID = viewModel.selectedBloodBank!!.id,
                        selectedBloodBank = viewModel.selectedBloodBank
                    )
                findNavController().navigate(action)
            } else {
                binding.root.showSnackBar(
                    binding.root,
                    getString(R.string.please_select_blood_bank),
                    Snackbar.LENGTH_SHORT,
                    null,
                    requireContext()
                ) {}
            }
        }
    }

    //
    private fun setupRecyclerView() = binding.recyclerViewBloodBanks.apply {
        bloodBankAdapter = BloodBankAdapter(this@AppointmentLocationFragment)
        adapter = bloodBankAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onItemClick(bloodBank: BloodBank) {
        viewModel.onBloodBankSelected(bloodBank)
    }

    override fun onPhoneNumberImageClick(bloodBank: BloodBank) {
        viewModel.onBloodBankPhoneNumSelected(bloodBank)
    }

    override fun onBloodBankPlaceImageClick(bloodBank: BloodBank) {
        viewModel.onBloodBankLocationSelected(bloodBank)
    }
}