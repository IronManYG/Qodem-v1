package com.example.qodem.ui.appointment

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.qodem.data.bloodbanks.repository.BloodBankRepository
import com.example.qodem.data.userinfo.remote.DonationNetworkEntity
import com.example.qodem.data.userinfo.repository.UserInfoRepository
import com.example.qodem.model.BloodBank
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class AppointmentLocationViewModel
@Inject
constructor(
    private val bloodBankRepository: BloodBankRepository,
    private val userInfoRepository: UserInfoRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    //
    val bloodBanksList: LiveData<List<BloodBank>> = bloodBankRepository.bloodBanks

    //
    val donationSaveState: LiveData<Boolean> = userInfoRepository.donationSaved

    //
    val saveErrorMessage: LiveData<String?> = userInfoRepository.saveErrorMessage

    suspend fun saveDonation(donationNetworkEntity: DonationNetworkEntity) {
        userInfoRepository.saveDonation(donationNetworkEntity)
    }

    suspend fun getAllDonations() {
        userInfoRepository.getAllDonations()
    }

}