package com.example.qodem.ui.appointment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qodem.databinding.FragmentAppointmentDateBinding
import com.example.qodem.model.AppointmentDay
import com.example.qodem.model.AppointmentTime
import com.example.qodem.model.BloodBank
import com.example.qodem.ui.AppointmentDayAdapter
import com.example.qodem.ui.AppointmentTimeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AppointmentDateFragment : Fragment(), AppointmentDayAdapter.OnItemClickListener, AppointmentTimeAdapter.OnItemClickListener {

    companion object {
        const val TAG = "AppointmentDateFrag"
    }

    private val viewModel: AppointmentLocationViewModel by viewModels()

    private lateinit var binding: FragmentAppointmentDateBinding

    //
    private lateinit var appointmentDayAdapter: AppointmentDayAdapter

    //
    private lateinit var appointmentTimeAdapter: AppointmentTimeAdapter

    //
    private lateinit var selectedBloodBank: BloodBank

    private val args: AppointmentDateFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAppointmentDateBinding.inflate(layoutInflater)

        //
        for(bloodBank in viewModel.bloodBanksList.value!!){
            if(bloodBank.id == args.bloodBankID){
                selectedBloodBank = bloodBank
                Log.d("here", "bloodBank id: ${bloodBank.id}")
            }
        }

        appointmentTimesList()

        //
        setupDaysRecyclerView()
        setupTimesRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        appointmentDayAdapter.appointmentDays = appointmentDaysList()
        appointmentTimeAdapter.appointmentTimes = appointmentTimesList()
    }

    //
    private fun setupDaysRecyclerView() = binding.recyclerViewDaySelector.apply {
        appointmentDayAdapter = AppointmentDayAdapter(this@AppointmentDateFragment)
        adapter = appointmentDayAdapter
        layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
    }

    //
    private fun setupTimesRecyclerView() = binding.recyclerViewTimeSelector.apply {
        appointmentTimeAdapter = AppointmentTimeAdapter(this@AppointmentDateFragment)
        adapter = appointmentTimeAdapter
        layoutManager = GridLayoutManager(requireContext(),3)
    }

    override fun onDayItemClick(position: Int) {
        appointmentDayAdapter.appointmentDays.forEach{
            it.isSelected = it == appointmentDayAdapter.appointmentDays[position]
        }
        Toast.makeText(requireContext(), "Day position $position",Toast.LENGTH_SHORT ).show()
    }

    override fun onTimeItemClick(position: Int) {
        appointmentTimeAdapter.appointmentTimes.forEach{
            it.isSelected = it == appointmentTimeAdapter.appointmentTimes[position]
        }
        Toast.makeText(requireContext(), "Time position $position",Toast.LENGTH_SHORT ).show()
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
        val daysList: MutableList<AppointmentDay> = daysListAsLong.map{AppointmentDay(it)}.toMutableList()
        return daysList.toList()
    }

    private fun appointmentTimesList(): List<AppointmentTime> {
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val startTime =  selectedBloodBank.workingHours.startTime
        val endTime = selectedBloodBank.workingHours.endTime
        val workingHours = endTime - startTime
        var startTimeInMilli = startTime * hoursInMilli
        val endTimeInMilli = endTime * hoursInMilli
        val workingHoursInMilli = endTimeInMilli - startTimeInMilli
        val timesListAsLong: MutableList<Long> = listOf(startTimeInMilli).toMutableList()
        for(i in (workingHours*2) downTo 1) {
            startTimeInMilli += hoursInMilli/2
            timesListAsLong.add(startTimeInMilli)
        }
        val timesList: MutableList<AppointmentTime> = timesListAsLong.map{AppointmentTime(it)}.toMutableList()
        return timesList.toList()
    }
}