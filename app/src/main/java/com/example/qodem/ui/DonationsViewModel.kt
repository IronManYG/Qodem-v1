package com.example.qodem.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.qodem.data.userinfo.remote.DonationNetworkEntity
import com.example.qodem.data.userinfo.repository.UserInfoRepository
import com.example.qodem.model.Donation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    val donations: LiveData<List<Donation>> = userInfoRepository.donations

    val authenticatedDonations: LiveData<List<Donation>> = userInfoRepository.authenticatedDonations

    val activeDonation: LiveData<Donation> = userInfoRepository.activeDonation

    //
    val donationsFoundState: LiveData<Boolean> = userInfoRepository.donationsFound

    val donationSaveState: LiveData<Boolean> = userInfoRepository.donationSaved

    //
    val errorResultMessage: LiveData<String?> = userInfoRepository.errorResultMessage

    val saveErrorMessage: LiveData<String?> = userInfoRepository.saveErrorMessage

    suspend fun getAllDonations() {
        userInfoRepository.getAllDonations()
    }

    suspend fun saveDonation(donationNetworkEntity: DonationNetworkEntity) {
        userInfoRepository.saveDonation(donationNetworkEntity)
    }
}