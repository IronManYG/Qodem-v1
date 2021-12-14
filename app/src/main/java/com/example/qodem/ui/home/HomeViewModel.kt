package com.example.qodem.ui.home

import androidx.lifecycle.*
import com.example.qodem.data.repository.MainRepository
import com.example.qodem.model.BloodBank
import com.example.qodem.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.qodem.ui.home.MainStateEvent.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private val _dataState: MutableLiveData<DataState<List<BloodBank>>> = MutableLiveData()

    val dataState: LiveData<DataState<List<BloodBank>>>
        get() = _dataState

    fun setStateEvent(mainStateEvent: MainStateEvent) {
        viewModelScope.launch{
            when(mainStateEvent){
                is GetBloodBanksEvent -> {
                    mainRepository.getBloodBanks()
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
                None -> {
                    //
                }
            }
        }
    }
}

sealed class MainStateEvent{

    object GetBloodBanksEvent: MainStateEvent()

    object None: MainStateEvent()
}
