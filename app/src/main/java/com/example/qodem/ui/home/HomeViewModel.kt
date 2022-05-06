package com.example.qodem.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qodem.data.Language
import com.example.qodem.data.PreferencesManager
import com.example.qodem.data.bloodbanks.repository.BloodBankRepository
import com.example.qodem.data.userinfo.repository.UserInfoRepository
import com.example.qodem.model.BloodBank
import com.example.qodem.model.Donation
import com.example.qodem.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val bloodBankRepository: BloodBankRepository,
    private val userInfoRepository: UserInfoRepository,
    private val preferencesManager: PreferencesManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    //
    private val bloodBanksFlow = bloodBankRepository.bloodBanks
    val bloodBanksList: StateFlow<List<BloodBank>> = bloodBanksFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val activeDonation: LiveData<Donation> = userInfoRepository.activeDonation

    val donation: LiveData<List<Donation>> = userInfoRepository.donations

    val userInfo: LiveData<User> = userInfoRepository.userInfo

    //
    val activeDonationFoundState: LiveData<Boolean> = userInfoRepository.activeDonationFound
    val donationUpdatedState: LiveData<Boolean> = userInfoRepository.donationUpdated

    //
    val errorResultMessage: LiveData<String?> = bloodBankRepository.errorResultMessage
    val updateErrorMessage: LiveData<String?> = userInfoRepository.updateErrorMessage

    private val preferencesFlow = preferencesManager.preferencesFlow

    private val languageFlow = preferencesFlow.map {
        it.language
    }
    val language: StateFlow<Language> = languageFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            Language.Arabic
        )

    fun getBloodBanks() = viewModelScope.launch {
        bloodBankRepository.getBloodBanks()
    }

    suspend fun updateDonationActiveState(donationID: String, isActive: Boolean) {
        userInfoRepository.updateDonationActiveState(donationID, isActive)
    }

    fun clearBloodBanks() = viewModelScope.launch {
        bloodBankRepository.clearBloodBanks()
    }

    suspend fun clearUserInfo() {
        userInfoRepository.clearUserInfo()
    }
}
