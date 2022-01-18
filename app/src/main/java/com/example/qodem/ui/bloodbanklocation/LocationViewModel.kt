package com.example.qodem.ui.bloodbanklocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.qodem.data.bloodbanks.repository.BloodBankRepository
import com.example.qodem.model.BloodBank
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class LocationViewModel
@Inject
constructor(
    private val bloodBankRepository: BloodBankRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel(){

    //
    val bloodBanksList: LiveData<List<BloodBank>> = bloodBankRepository.bloodBanks

    //
    val bloodBanksGetState: LiveData<Boolean> = bloodBankRepository.bloodBanksFound

    //
    val errorResultMessage: LiveData<String?> = bloodBankRepository.errorResultMessage
}