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
import com.example.qodem.utils.ConnectionLiveData
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
    private val connectionLiveData: ConnectionLiveData,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val connectionState = connectionLiveData

    //
    private val bloodBanksFlow = bloodBankRepository.bloodBanks
    val bloodBanksList: StateFlow<List<BloodBank>> = bloodBanksFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val activeDonationFlow = userInfoRepository.activeDonation
    val activeDonation: StateFlow<Donation> = activeDonationFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            Donation(
                "",
                "",
                "",
                "",
                false,
                false,
                -1
            )
        )

    private val donationFlow = userInfoRepository.donations
    val donation: StateFlow<List<Donation>> = donationFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val userInfoFlow = userInfoRepository.userInfo
    val userInfo: StateFlow<User> = userInfoFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            User(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                -1,
                "",
                "",
                -1,
                emptyList()
            )
        )

    //
    val activeDonationFoundState = userInfoRepository.activeDonationFound
    val donationUpdatedState = userInfoRepository.donationUpdated

    //
    val errorResultMessage = bloodBankRepository.errorResultMessage
    val updateErrorMessage = userInfoRepository.updateErrorMessage

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
