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
import com.example.qodem.ui.AppointmentDayAdapter
import com.example.qodem.ui.AppointmentTimeAdapter
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

    // selected appointment Day by user
    private var appointmentDay: Long = 0L

    // selected appointment Time by user
    private var appointmentTime: Long = 0L

    private val args: AppointmentDateFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inflate the layout for this fragment
        binding = FragmentAppointmentDateBinding.bind(view)

        setupDaysRecyclerView()
        setupTimesRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bloodBanksList.collect { bloodBanks ->
                    for (bloodBank in bloodBanks) {
                        if (bloodBank.id == args.bloodBankID) {
                            selectedBloodBank = bloodBank
                            appointmentTimeAdapter.appointmentTimes = appointmentTimesList()
                            Log.d("here", "bloodBank id: ${bloodBank.id}")
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
                    timeStamp = appointmentDay + appointmentTime
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
        appointmentDayAdapter.appointmentDays = appointmentDaysList()
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

    override fun onDayItemClick(position: Int) {
        appointmentDayAdapter.appointmentDays.forEach {
            it.isSelected = it == appointmentDayAdapter.appointmentDays[position]
        }
        isAppointmentDaySelected = true

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = Date(appointmentDayAdapter.appointmentDays[position].dayInMilli)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        appointmentDay = calendar.timeInMillis
        Log.d("here Date", "day in mill is: $appointmentDay ")
    }

    override fun onTimeItemClick(position: Int) {
        appointmentTimeAdapter.appointmentTimes.forEach {
            it.isSelected = it == appointmentTimeAdapter.appointmentTimes[position]
        }
        isAppointmentTimeSelected = true
        appointmentTime = appointmentTimeAdapter.appointmentTimes[position].timeInMilli
        Log.d("here Date", "time in mill is: $appointmentTime")
    }

    private fun appointmentDaysList(): List<AppointmentDay> {
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val currentTime = Calendar.getInstance().timeInMillis
        val daysListAsLong: MutableList<Long> = listOf(currentTime).toMutableList()
        var nextDay = currentTime + daysInMilli
        for (i in 13 downTo 1) {
            daysListAsLong.add(nextDay)
            nextDay += daysInMilli
        }
        val daysList: MutableList<AppointmentDay> =
            daysListAsLong.map { AppointmentDay(it) }.toMutableList()
        return daysList.toList()
    }

    private fun appointmentTimesList(): List<AppointmentTime> {
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val startTime = selectedBloodBank.workingHours.startTime
        val endTime = selectedBloodBank.workingHours.endTime
        val workingHours = endTime - startTime
        var startTimeInMilli = startTime * hoursInMilli
        val endTimeInMilli = endTime * hoursInMilli
        val workingHoursInMilli = endTimeInMilli - startTimeInMilli
        val timesListAsLong: MutableList<Long> = listOf(startTimeInMilli).toMutableList()
        for (i in (workingHours * 2) downTo 1) {
            startTimeInMilli += hoursInMilli / 2
            timesListAsLong.add(startTimeInMilli)
        }
        val timesList: MutableList<AppointmentTime> =
            timesListAsLong.map { AppointmentTime(it) }.toMutableList()
        return timesList.toList()
    }
}