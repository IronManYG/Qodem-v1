package com.example.qodem.ui.bloodbanklocation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qodem.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class LocationViewModel
@Inject
constructor(
    private val mainRepository: MainRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel(){
//    private val _dataState: MutableStateFlow<DataState<List<BloodBank>>> = MutableStateFlow(
//        DataState.Success(emptyList()))
//
//    val dataState: StateFlow<DataState<List<BloodBank>>> = _dataState

    init {
        viewModelScope.launch{
//            setStateEvent(MainStateEvent.GetBloodBanksEvent)
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
//                    SharingStarted.WhileSubscribed(5000),
//                    DataState.Success(emptyList())
//                )
//
//            when(mainStateEvent){
//                is MainStateEvent.GetBloodBanksEvent -> {
//                    result
//                        .onEach { dataState ->
//                            _dataState.value = dataState
//                        }
//                        .launchIn(viewModelScope)
//                }
//                MainStateEvent.None -> {
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