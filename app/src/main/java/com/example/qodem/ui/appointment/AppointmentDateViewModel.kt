package com.example.qodem.ui.appointment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qodem.data.bloodbanks.repository.BloodBankRepository
import com.example.qodem.data.userinfo.remote.DonationNetworkEntity
import com.example.qodem.data.userinfo.repository.UserInfoRepository
import com.example.qodem.model.AppointmentDay
import com.example.qodem.model.AppointmentTime
import com.example.qodem.model.BloodBank
import com.example.qodem.utils.appointmentDaysList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class AppointmentDateViewModel
@Inject
constructor(
    private val bloodBankRepository: BloodBankRepository,
    private val userInfoRepository: UserInfoRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var appointmentDaysList = mutableListOf<AppointmentDay>()

    var appointmentTimesList = mutableListOf<AppointmentTime>()

    var selectedDayInMillis: Long = -1L

    var selectedTimeInMillis: Long = -1L

    val donationSaveState = userInfoRepository.donationSaved

    val saveErrorMessage = userInfoRepository.saveErrorMessage

    private val appointmentDateEventsChannel = Channel<AppointmentDateEvent>()
    val appointmentDateEvents = appointmentDateEventsChannel.receiveAsFlow()

    private val bloodBanksFlow = bloodBankRepository.bloodBanks
    val bloodBanksList: StateFlow<List<BloodBank>> = bloodBanksFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun onAppointmentDaySelected(appointmentDay: AppointmentDay) = viewModelScope.launch {
        appointmentDateEventsChannel.send(AppointmentDateEvent.AppointmentDayIsSelected(appointmentDay))
    }

    fun onAppointmentTimeSelected(appointmentTime: AppointmentTime) = viewModelScope.launch {
        appointmentDateEventsChannel.send(AppointmentDateEvent.AppointmentTimeIsSelected(appointmentTime))
    }

    fun onAppointmentDaySelectionChanged(appointmentDay: AppointmentDay) = viewModelScope.launch {
        appointmentDaysList.forEach {
            it.isSelected = it == appointmentDay
        }
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = Date(appointmentDay.dayInMilli)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        selectedDayInMillis = calendar.timeInMillis
        Log.d("here Date", "day in mill is: $selectedDayInMillis ")
    }

    fun onAppointmentTimeSelectionChanged(appointmentTime: AppointmentTime) = viewModelScope.launch {
        appointmentTimesList.forEach {
            it.isSelected = it == appointmentTime
        }
        selectedTimeInMillis = appointmentTime.timeInMilli
    }

    suspend fun saveDonation(donationNetworkEntity: DonationNetworkEntity) {
        userInfoRepository.saveDonation(donationNetworkEntity)
    }

    fun getAllDonations() = viewModelScope.launch {
        userInfoRepository.getAllDonations()
    }

    sealed class AppointmentDateEvent {
        data class AppointmentDayIsSelected(val appointmentDay: AppointmentDay) : AppointmentDateEvent()
        data class AppointmentTimeIsSelected(val appointmentTime: AppointmentTime) : AppointmentDateEvent()
    }
}