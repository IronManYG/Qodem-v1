package com.example.qodem.ui.appointment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qodem.data.bloodbanks.repository.BloodBankRepository
import com.example.qodem.model.BloodBank
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class AppointmentLocationViewModel
@Inject
constructor(
    private val bloodBankRepository: BloodBankRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var selectedBloodBank: BloodBank? = null

    private val appointmentLocationsEventsChannel = Channel<AppointmentLocationEvent>()
    val appointmentLocationsEvents = appointmentLocationsEventsChannel.receiveAsFlow()

    private val bloodBanksFlow = bloodBankRepository.bloodBanks
    val bloodBanksList: StateFlow<List<BloodBank>> = bloodBanksFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun onBloodBankSelected(bloodBank: BloodBank) = viewModelScope.launch {
        appointmentLocationsEventsChannel.send(
            AppointmentLocationEvent.BloodBankIsSelected(
                bloodBank
            )
        )
    }

    fun onBloodBankLocationSelected(bloodBank: BloodBank) = viewModelScope.launch {
        appointmentLocationsEventsChannel.send(AppointmentLocationEvent.NavigateToMapsApp(bloodBank))
    }

    fun onBloodBankPhoneNumSelected(bloodBank: BloodBank) = viewModelScope.launch {
        appointmentLocationsEventsChannel.send(AppointmentLocationEvent.NavigateToDialApp(bloodBank))
    }

    fun onBloodBankSelectedChanged(bloodBank: BloodBank) = viewModelScope.launch {
        bloodBanksList.value.forEach {
            it.isSelected = it == bloodBank
        }
        selectedBloodBank = if (bloodBank.isSelected) bloodBank else null
    }

    sealed class AppointmentLocationEvent {
        data class BloodBankIsSelected(val bloodBank: BloodBank) : AppointmentLocationEvent()
        data class NavigateToMapsApp(val bloodBank: BloodBank) : AppointmentLocationEvent()
        data class NavigateToDialApp(val bloodBank: BloodBank) : AppointmentLocationEvent()
    }

}