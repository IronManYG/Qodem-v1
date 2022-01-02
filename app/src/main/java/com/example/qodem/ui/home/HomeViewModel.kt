package com.example.qodem.ui.home

import androidx.lifecycle.*
import com.example.qodem.data.bloodbanks.repository.BloodBankRepository
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
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    init {
        viewModelScope.launch{
            bloodBankRepository.getBloodBanks()
        }
    }

    val bloodBanksList = bloodBankRepository.bloodBanks
}
