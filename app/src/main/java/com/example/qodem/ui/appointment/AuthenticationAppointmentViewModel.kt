package com.example.qodem.ui.appointment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qodem.data.userinfo.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class AuthenticationAppointmentViewModel
@Inject
constructor(
    private val userInfoRepository: UserInfoRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val activeDonationID = savedStateHandle.get<String>("activeDonationID")

    //
    val donationUpdatedState = userInfoRepository.donationUpdated

    //
    val updateErrorMessage = userInfoRepository.updateErrorMessage

    fun updateDonationAuthenticatedState() = viewModelScope.launch {
        userInfoRepository.updateDonationAuthenticatedState(activeDonationID!!, true)
    }

    fun resetDonationUpdatedState() {
        userInfoRepository.resetDonationUpdatedState()
    }
}