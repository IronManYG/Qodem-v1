package com.example.qodem.ui.appointment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
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
import com.example.qodem.utils.ConnectionLiveData
import com.example.qodem.utils.appointmentDaysList
import com.example.qodem.utils.appointmentTimesList
import com.example.qodem.utils.showSnackBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

    private lateinit var appointmentDayAdapter: AppointmentDayAdapter

    private lateinit var appointmentTimeAdapter: AppointmentTimeAdapter

    private var connectionState = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inflate the layout for this fragment
        binding = FragmentAppointmentDateBinding.bind(view)

        setupDaysRecyclerView()
        setupTimesRecyclerView()

        appointmentDayAdapter.submitList(appointmentDaysList())
        viewModel.appointmentDaysList = appointmentDayAdapter.currentList

        appointmentTimeAdapter.submitList(appointmentTimesList(viewModel.selectedBloodBank!!))
        viewModel.appointmentTimesList = appointmentTimeAdapter.currentList

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                launch {
                    viewModel.connectionState.collect { isNetworkAvailable ->
                        when (isNetworkAvailable) {
                            true -> {
                                connectionState = true
                            }
                            false -> {
                                connectionState = false
                                binding.root.showSnackBar(
                                    binding.root,
                                    getString(R.string.network_not_available),
                                    Snackbar.LENGTH_LONG,
                                    null,
                                    requireContext()
                                ) {}
                            }
                        }
                    }
                }
            }
        }

        binding.apply {
            buttonBookAppointment.setOnClickListener {
                if(connectionState) {
                    when {
                        viewModel.selectedDayInMillis != null && viewModel.selectedTimeInMillis != null && viewModel.selectedBloodBank != null -> {
                            progressBar2.isVisible = true
                            buttonBookAppointment.isEnabled = false
                            viewModel.saveDonation()
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                                    viewModel.donationSaveState.collect {
                                        when (it) {
                                            true -> {
                                                viewModel.getAllDonations()
                                                findNavController().navigate(
                                                    AppointmentDateFragmentDirections.actionAppointmentDataFragmentToHomeFragment()
                                                )
                                            }
                                            false -> {
                                                if (viewModel.saveErrorMessage.value != "") {
                                                    root.showSnackBar(
                                                        root,
                                                        viewModel.saveErrorMessage.value!!,
                                                        Snackbar.LENGTH_SHORT,
                                                        null,
                                                        requireContext()
                                                    ) {}
                                                    progressBar2.isVisible = false
                                                    buttonBookAppointment.isEnabled = true
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        viewModel.selectedDayInMillis == null && viewModel.selectedTimeInMillis != null -> {
                            root.showSnackBar(
                                root,
                                getString(R.string.please_select_day),
                                Snackbar.LENGTH_SHORT,
                                null,
                                requireContext()
                            ) {}
                        }
                        viewModel.selectedDayInMillis != null && viewModel.selectedTimeInMillis == null -> {
                            root.showSnackBar(
                                root,
                                getString(R.string.please_select_time),
                                Snackbar.LENGTH_SHORT,
                                null,
                                requireContext()
                            ) {}
                        }
                        else -> {
                            root.showSnackBar(
                                root,
                                getString(R.string.please_select_day_and_time),
                                Snackbar.LENGTH_SHORT,
                                null,
                                requireContext()
                            ) {}
                        }
                    }

                } else {
                    binding.root.showSnackBar(
                        binding.root,
                        getString(R.string.network_not_available),
                        Snackbar.LENGTH_LONG,
                        null,
                        requireContext()
                    ) {}
                }
            }
        }
    }

    private fun setupDaysRecyclerView() = binding.recyclerViewDaySelector.apply {
        appointmentDayAdapter = AppointmentDayAdapter(this@AppointmentDateFragment)
        adapter = appointmentDayAdapter
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupTimesRecyclerView() = binding.recyclerViewTimeSelector.apply {
        appointmentTimeAdapter = AppointmentTimeAdapter(this@AppointmentDateFragment)
        adapter = appointmentTimeAdapter
        layoutManager = GridLayoutManager(requireContext(), 3)
    }

    override fun onDayItemClick(appointmentDay: AppointmentDay) {
        viewModel.onAppointmentDaySelected(appointmentDay)
    }

    override fun onTimeItemClick(appointmentTime: AppointmentTime) {
        viewModel.onAppointmentTimeSelected(appointmentTime)
    }
}