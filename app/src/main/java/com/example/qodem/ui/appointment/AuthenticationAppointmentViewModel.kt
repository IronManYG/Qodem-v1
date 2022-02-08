package com.example.qodem.ui.appointment

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.qodem.data.bloodbanks.repository.BloodBankRepository
import com.example.qodem.data.userinfo.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class AuthenticationAppointmentViewModel
@Inject
constructor(
    private val userInfoRepository: UserInfoRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel(){

    //
    val donationUpdatedState: LiveData<Boolean> = userInfoRepository.donationUpdated

    //
    val updateErrorMessage: LiveData<String?> = userInfoRepository.updateErrorMessage

    suspend fun updateDonationAuthenticatedState(donationID: String, isActive: Boolean){
        userInfoRepository.updateDonationAuthenticatedState(donationID,isActive)
    }

    suspend fun getAllDonations() {
        userInfoRepository.getAllDonations()
    }

    fun resetDonationUpdatedState(){
        userInfoRepository.resetDonationUpdatedState()
    }
}