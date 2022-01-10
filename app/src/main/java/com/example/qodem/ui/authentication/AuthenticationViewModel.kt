package com.example.qodem.ui.authentication

import android.util.Log
import androidx.lifecycle.*
import com.example.qodem.data.bloodbanks.repository.BloodBankRepository
import com.example.qodem.data.userinfo.repository.UserInfoRepository
import com.google.firebase.auth.PhoneAuthCredential
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
):  ViewModel() {

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val userInfoState: LiveData<Boolean> = userInfoRepository.userInfoFound

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

    suspend fun getUser(phoneNumber: String){
        if (phoneNumber != ""){
            userInfoRepository.getUserInfo(phoneNumber)
        }
    }

}