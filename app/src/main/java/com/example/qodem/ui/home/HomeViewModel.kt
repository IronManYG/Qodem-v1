package com.example.qodem.ui.home

import androidx.lifecycle.*
import com.example.qodem.data.repository.MainRepository
import com.example.qodem.model.BloodBank
import com.example.qodem.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
//import com.example.qodem.ui.home.MainStateEvent.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
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

//    private val _dataState: MutableStateFlow<DataState<List<BloodBank>>> = MutableStateFlow(DataState.Success(emptyList()))
//
//    val dataState: StateFlow<DataState<List<BloodBank>>> = _dataState

    init {
        viewModelScope.launch{
//            setStateEvent(GetBloodBanksEvent)
            mainRepository.getBloodBanks()
        }
    }

    val bloodBanksList = mainRepository.bloodBanks

//    fun setStateEvent(mainStateEvent: MainStateEvent) {
//
//        viewModelScope.launch{
//
//            val result: StateFlow<DataState<List<BloodBank>>> = mainRepository.getBloodBanks()
//                .stateIn(
//                    viewModelScope,
//                    WhileSubscribed(5000),
//                    DataState.Success(emptyList())
//            )
//
//            when(mainStateEvent){
//                is GetBloodBanksEvent -> {
//                    result
//                        .onEach { dataState ->
//                        _dataState.value = dataState
//                    }
//                        .launchIn(viewModelScope)
//                }
//                None -> {
//                    //
//                }
//            }
//        }
//    }
}

//sealed class MainStateEvent{
//
//    object GetBloodBanksEvent: MainStateEvent()
//
//    object None: MainStateEvent()
//}
