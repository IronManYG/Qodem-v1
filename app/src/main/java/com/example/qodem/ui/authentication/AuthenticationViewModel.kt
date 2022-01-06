package com.example.qodem.ui.authentication

import androidx.lifecycle.*
import com.example.qodem.data.bloodbanks.repository.BloodBankRepository
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
):  ViewModel() {

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val userInfoState: MutableLiveData<Boolean> = MutableLiveData(userInfoRepository.userInfoFound)

    //  Create an authenticationState variable based off the FirebaseUserLiveData object. By
    //  creating this variable, other classes will be able to query for whether the user is logged
    //  in or not

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    suspend fun getUser(phoneNumber: String){
        userInfoRepository.getUserInfo(phoneNumber)
    }

}