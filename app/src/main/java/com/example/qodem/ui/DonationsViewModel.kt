package com.example.qodem.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qodem.data.userinfo.remote.DonationNetworkEntity
import com.example.qodem.data.userinfo.repository.UserInfoRepository
import com.example.qodem.model.Donation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DonationsViewModel
@Inject
constructor(
    private val userInfoRepository: UserInfoRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    //
    private val donationFlow = userInfoRepository.donations
    val donations: StateFlow<List<Donation>> = donationFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val authenticatedDonationsFlow = userInfoRepository.authenticatedDonations
    val authenticatedDonations: StateFlow<List<Donation>> = authenticatedDonationsFlow
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

    //
    val donationsFoundState: LiveData<Boolean> = userInfoRepository.donationsFound

    val donationSaveState: LiveData<Boolean> = userInfoRepository.donationSaved

    //
    val errorResultMessage: LiveData<String?> = userInfoRepository.errorResultMessage

    val saveErrorMessage: LiveData<String?> = userInfoRepository.saveErrorMessage

    fun getAllDonations() = viewModelScope.launch {
        userInfoRepository.getAllDonations()
    }

    suspend fun saveDonation(donationNetworkEntity: DonationNetworkEntity) {
        userInfoRepository.saveDonation(donationNetworkEntity)
    }
}