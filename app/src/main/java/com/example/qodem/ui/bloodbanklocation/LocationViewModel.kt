package com.example.qodem.ui.bloodbanklocation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.qodem.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class LocationViewModel
@Inject
constructor(
    private val mainRepository: MainRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel(){

    val bloodBanksList = mainRepository.bloodBanks

}