package com.example.qodem.ui.home

import androidx.lifecycle.*
import com.example.qodem.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
//import com.example.qodem.ui.home.MainStateEvent.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val mainRepository: MainRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    init {
        viewModelScope.launch{
            mainRepository.getBloodBanks()
        }
    }

    val bloodBanksList = mainRepository.bloodBanks
}
