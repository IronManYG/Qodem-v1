package com.example.qodem.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qodem.data.bloodbanks.repository.BloodBankRepository
import com.example.qodem.data.userinfo.repository.UserInfoRepository
import com.example.qodem.model.BloodBank
import com.example.qodem.model.Donation
import com.example.qodem.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val bloodBankRepository: BloodBankRepository,
    private val userInfoRepository: UserInfoRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    init {
        viewModelScope.launch{
            bloodBankRepository.getBloodBanks()
        }
    }

    //
    val bloodBanksList: LiveData<List<BloodBank>> = bloodBankRepository.bloodBanks

    val activeDonation : LiveData<Donation> = userInfoRepository.activeDonation

    val donation : LiveData<List<Donation>> = userInfoRepository.donations

    val userInfo: LiveData<User> = userInfoRepository.userInfo

    //
    val activeDonationFoundState: LiveData<Boolean> = userInfoRepository.activeDonationFound
    val donationUpdatedState: LiveData<Boolean> = userInfoRepository.donationUpdated

    //
    val errorResultMessage: LiveData<String?> = bloodBankRepository.errorResultMessage
    val updateErrorMessage: LiveData<String?> = userInfoRepository.updateErrorMessage

    suspend fun updateDonationActiveState(donationID: String, isActive: Boolean){
        userInfoRepository.updateDonationActiveState(donationID,isActive)
    }

    suspend fun clearBloodBanks(){
        bloodBankRepository.clearBloodBanks()
    }

    suspend fun clearUserInfo() {
        userInfoRepository.clearUserInfo()
    }
}
