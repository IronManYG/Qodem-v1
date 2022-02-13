package com.example.qodem.ui.settingsandoptions.userinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.qodem.data.bloodbanks.repository.BloodBankRepository
import com.example.qodem.data.userinfo.repository.UserInfoRepository
import com.example.qodem.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class UserInfoViewModel
@Inject
constructor(
    private val bloodBankRepository: BloodBankRepository,
    private val userInfoRepository: UserInfoRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    //
    val userInfo: LiveData<User> = userInfoRepository.userInfo

    //
    val userInfoUpdated: LiveData<Boolean> = userInfoRepository.userInfoUpdated

    //
    val errorResultMessage: LiveData<String?> = bloodBankRepository.errorResultMessage
    val updateErrorMessage: LiveData<String?> = userInfoRepository.updateErrorMessage

    suspend fun updateUserName(userID: String, firstName: String, lastName: String, phoneNumber: String){
        userInfoRepository.updateUserName(userID,firstName,lastName, phoneNumber)
    }

    suspend fun updateUserDateOFBirth(userID: String, dateOfBirth: String, phoneNumber: String){
        userInfoRepository.updateUserDateOFBirth(userID,dateOfBirth, phoneNumber)
    }

    suspend fun updateUserBloodType(userID: String, bloodType: String, phoneNumber: String){
        userInfoRepository.updateUserBloodType(userID,bloodType, phoneNumber)
    }

    suspend fun updateUserGender(userID: String, gender: String, phoneNumber: String){
        userInfoRepository.updateUserGender(userID,gender, phoneNumber)
    }

    suspend fun updateUserCity(userID: String, city: String, phoneNumber: String){
        userInfoRepository.updateUserCity(userID,city, phoneNumber)
    }

    suspend fun updateUserID(userID: String, idType: String, idNumber: String, phoneNumber: String){
        userInfoRepository.updateUserID(userID,idType,idNumber, phoneNumber)
    }

}