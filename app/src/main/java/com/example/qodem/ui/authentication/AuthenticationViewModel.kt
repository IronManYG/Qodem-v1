package com.example.qodem.ui.authentication

import android.util.Log
import androidx.lifecycle.*
import com.example.qodem.data.userinfo.remote.UserNetworkEntity
import com.example.qodem.data.userinfo.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class AuthenticationViewModel
@Inject
constructor(
    private val userInfoRepository: UserInfoRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    //
    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    //
    val userInfoFoundState: LiveData<Boolean> = userInfoRepository.userInfoFound

    val userInfoSaveState: LiveData<Boolean> = userInfoRepository.userInfoSaved

    //
    val errorResultMessage: LiveData<String?> = userInfoRepository.errorResultMessage

    val saveErrorMessage: LiveData<String?> = userInfoRepository.saveErrorMessage

    //
    private var _userPhoneNumber: MutableLiveData<String> = MutableLiveData<String>()
    val userPhoneNumber: LiveData<String>
        get() = _userPhoneNumber

    //  Create an authenticationState variable based off the FirebaseUserLiveData object. By
    //  creating this variable, other classes will be able to query for whether the user is logged
    //  in or not

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            _userPhoneNumber.postValue(user.phoneNumber)
            Log.d("here", "phone number is ${user.phoneNumber}")
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    suspend fun getUserInfo(phoneNumber: String) {
        if (phoneNumber != "") {
            userInfoRepository.getUserInfo(phoneNumber)
        }
    }

    suspend fun saveUserInfo(userNetworkEntity: UserNetworkEntity) {
        userInfoRepository.saveUserInfo(userNetworkEntity)
    }

    fun getAllDonations() = viewModelScope.launch {
        userInfoRepository.getAllDonations()
    }

}