package com.example.qodem.ui.appointment

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.qodem.data.bloodbanks.repository.BloodBankRepository
import com.example.qodem.data.userinfo.remote.DonationNetworkEntity
import com.example.qodem.data.userinfo.repository.UserInfoRepository
import com.example.qodem.di.module.ApplicationScope
import com.example.qodem.model.AppointmentDay
import com.example.qodem.model.AppointmentTime
import com.example.qodem.model.BloodBank
import com.example.qodem.utils.ConnectionLiveData
import com.example.qodem.utils.appointmentDaysList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
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
    private val userInfoRepository: UserInfoRepository,
    private val connectionLiveData: ConnectionLiveData,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationScope private val applicationScope: CoroutineScope
) : ViewModel() {

    companion object {
        const val TAG = "AppointmentDateViewModel"
    }

    val connectionState = connectionLiveData.asFlow()

    val selectedBloodBank = savedStateHandle.get<BloodBank>("selectedBloodBank")

    var appointmentDaysList = mutableListOf<AppointmentDay>()

    var appointmentTimesList = mutableListOf<AppointmentTime>()

    var selectedDayInMillis: Long? = null

    var selectedTimeInMillis: Long? = null

    val donationSaveState = userInfoRepository.donationSaved

    val saveErrorMessage = userInfoRepository.saveErrorMessage

    private val appointmentDateEventsChannel = Channel<AppointmentDateEvent>()
    val appointmentDateEvents = appointmentDateEventsChannel.receiveAsFlow()

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
        Log.d(TAG, "day in mill is: $selectedDayInMillis ")
    }

    fun onAppointmentTimeSelectionChanged(appointmentTime: AppointmentTime) = viewModelScope.launch {
        appointmentTimesList.forEach {
            it.isSelected = it == appointmentTime
        }
        selectedTimeInMillis = appointmentTime.timeInMilli
    }

    fun saveDonation() = applicationScope.launch {
        val donationNetworkEntity = DonationNetworkEntity(
            bloodBankID = "${selectedBloodBank!!.id}",
            donationData = "",
            donationTime = "",
            active = true,
            authenticated = false,
            timeStamp = selectedDayInMillis!! + selectedTimeInMillis!!
        )
        userInfoRepository.saveDonation(donationNetworkEntity)
    }

    fun getAllDonations() = applicationScope.launch {
        userInfoRepository.getAllDonations()
    }

    sealed class AppointmentDateEvent {
        data class AppointmentDayIsSelected(val appointmentDay: AppointmentDay) : AppointmentDateEvent()
        data class AppointmentTimeIsSelected(val appointmentTime: AppointmentTime) : AppointmentDateEvent()
    }
}