package com.example.qodem.ui.appointment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qodem.R
import com.example.qodem.data.userinfo.remote.DonationNetworkEntity
import com.example.qodem.databinding.FragmentAppointmentDateBinding
import com.example.qodem.model.AppointmentDay
import com.example.qodem.model.AppointmentTime
import com.example.qodem.model.BloodBank
import com.example.qodem.utils.appointmentDaysList
import com.example.qodem.utils.appointmentTimesList
import com.example.qodem.utils.showSnackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AppointmentDateFragment : Fragment(R.layout.fragment_appointment_date),
    AppointmentDayAdapter.OnItemClickListener,
    AppointmentTimeAdapter.OnItemClickListener {

    companion object {
        const val TAG = "AppointmentDateFrag"
    }

    private val viewModel: AppointmentDateViewModel by viewModels()

    private lateinit var binding: FragmentAppointmentDateBinding

    //
    private lateinit var appointmentDayAdapter: AppointmentDayAdapter

    //
    private lateinit var appointmentTimeAdapter: AppointmentTimeAdapter

    //
    private lateinit var selectedBloodBank: BloodBank

    //
    private var isAppointmentDaySelected = false
    private var isAppointmentTimeSelected = false

    private val args: AppointmentDateFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inflate the layout for this fragment
        binding = FragmentAppointmentDateBinding.bind(view)

        setupDaysRecyclerView()
        setupTimesRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.bloodBanksList.collect { bloodBanks ->
                        for (bloodBank in bloodBanks) {
                            if (bloodBank.id == args.bloodBankID) {
                                selectedBloodBank = bloodBank
                                appointmentTimeAdapter.submitList(appointmentTimesList(selectedBloodBank))
                                viewModel.appointmentTimesList = appointmentTimeAdapter.currentList
                                Log.d("here", "bloodBank id: ${bloodBank.id}")
                            }
                        }
                    }
                }
                launch {
                    viewModel.appointmentDateEvents.collect { event ->
                        when (event) {
                            is AppointmentDateViewModel.AppointmentDateEvent.AppointmentDayIsSelected -> {
                                viewModel.onAppointmentDaySelectionChanged(event.appointmentDay)
                            }
                            is AppointmentDateViewModel.AppointmentDateEvent.AppointmentTimeIsSelected -> {
                                viewModel.onAppointmentTimeSelectionChanged(event.appointmentTime)
                            }
                        }
                    }
                }
            }
        }

        binding.buttonBookAppointment.setOnClickListener {
            if (isAppointmentDaySelected && isAppointmentTimeSelected) {
                binding.progressBar2.visibility = View.VISIBLE
                binding.buttonBookAppointment.isEnabled = false
                val donationNetworkEntity = DonationNetworkEntity(
                    bloodBankID = "${args.bloodBankID}",
                    donationData = "",
                    donationTime = "",
                    active = true,
                    authenticated = false,
                    timeStamp = viewModel.selectedDayInMillis + viewModel.selectedTimeInMillis
                )
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.Main) {
                        viewModel.saveDonation(donationNetworkEntity)
                        viewLifecycleOwner.lifecycleScope.launch {
                            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                                viewModel.donationSaveState.collect {
                                    when (it) {
                                        true -> {
                                            CoroutineScope(Dispatchers.IO).launch {
                                                withContext(Dispatchers.IO) {
                                                    viewModel.getAllDonations()
                                                }
                                            }
                                            findNavController().navigate(
                                                AppointmentDateFragmentDirections.actionAppointmentDataFragmentToHomeFragment()
                                            )
                                        }
                                        false -> {
                                            binding.root.showSnackbar(
                                                binding.root,
                                                viewModel.saveErrorMessage.value.toString(),
                                                Snackbar.LENGTH_SHORT,
                                                null,
                                                requireContext()
                                            ) {}
                                        }
                                    }
                                }
                            }
                        }
                        binding.progressBar2.visibility = View.GONE
                        binding.buttonBookAppointment.isEnabled = true
                    }
                }
            } else if (!isAppointmentDaySelected && isAppointmentTimeSelected) {
                binding.root.showSnackbar(
                    binding.root,
                    getString(R.string.please_select_day),
                    Snackbar.LENGTH_SHORT,
                    null,
                    requireContext()
                ) {}
            } else if (isAppointmentDaySelected && !isAppointmentTimeSelected) {
                binding.root.showSnackbar(
                    binding.root,
                    getString(R.string.please_select_time),
                    Snackbar.LENGTH_SHORT,
                    null,
                    requireContext()
                ) {}
            } else {
                binding.root.showSnackbar(
                    binding.root,
                    getString(R.string.please_select_day_and_time),
                    Snackbar.LENGTH_SHORT,
                    null,
                    requireContext()
                ) {}
            }
        }

        //
        appointmentDayAdapter.submitList(appointmentDaysList())

        viewModel.appointmentDaysList = appointmentDayAdapter.currentList
    }

    //
    private fun setupDaysRecyclerView() = binding.recyclerViewDaySelector.apply {
        appointmentDayAdapter = AppointmentDayAdapter(this@AppointmentDateFragment)
        adapter = appointmentDayAdapter
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    //
    private fun setupTimesRecyclerView() = binding.recyclerViewTimeSelector.apply {
        appointmentTimeAdapter = AppointmentTimeAdapter(this@AppointmentDateFragment)
        adapter = appointmentTimeAdapter
        layoutManager = GridLayoutManager(requireContext(), 3)
    }


    override fun onDayItemClick(appointmentDay: AppointmentDay) {
        viewModel.onAppointmentDaySelected(appointmentDay)
        isAppointmentDaySelected = true
    }

    override fun onTimeItemClick(appointmentTime: AppointmentTime) {
        viewModel.onAppointmentTimeSelected(appointmentTime)
        isAppointmentTimeSelected = true
    }
}