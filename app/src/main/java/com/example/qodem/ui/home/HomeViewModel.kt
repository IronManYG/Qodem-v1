package com.example.qodem.ui.home

import androidx.lifecycle.*
import com.example.qodem.data.bloodbanks.repository.BloodBankRepository
import com.example.qodem.data.userinfo.repository.UserInfoRepository
import com.example.qodem.model.BloodBank
import com.example.qodem.model.Donation
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

    //
    val activeDonationFoundState: LiveData<Boolean> = userInfoRepository.activeDonationFound

    //
    val errorResultMessage: LiveData<String?> = bloodBankRepository.errorResultMessage

}
