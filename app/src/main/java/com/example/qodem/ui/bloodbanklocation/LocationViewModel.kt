package com.example.qodem.ui.bloodbanklocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qodem.data.bloodbanks.repository.BloodBankRepository
import com.example.qodem.data.userinfo.repository.UserInfoRepository
import com.example.qodem.model.BloodBank
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class LocationViewModel
@Inject
constructor(
    private val bloodBankRepository: BloodBankRepository,
    private val userInfoRepository: UserInfoRepository,
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

    //
    val activeDonationFoundState: LiveData<Boolean> = userInfoRepository.activeDonationFound

    //
    val errorResultMessage: LiveData<String?> = bloodBankRepository.errorResultMessage
}