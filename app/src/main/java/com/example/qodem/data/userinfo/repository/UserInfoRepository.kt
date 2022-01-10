package com.example.qodem.data.userinfo.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.qodem.data.userinfo.local.UserCacheMapper
import com.example.qodem.data.userinfo.local.UserDao
import com.example.qodem.data.userinfo.remote.UserFirestore
import com.example.qodem.data.userinfo.remote.UserNetworkEntity
import com.example.qodem.data.userinfo.remote.UserNetworkMapper
import com.example.qodem.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.qodem.utils.Result

class UserInfoRepository
constructor(
    private val userDao: UserDao,
    private val userFirestore: UserFirestore,
    private val userCacheMapper: UserCacheMapper,
    private val userNetworkMapper: UserNetworkMapper
) {

    val userInfo : LiveData<User> = Transformations.map(userDao.getUserInfo()){
        userCacheMapper.mapFromEntity(it)
    }

    private var _userInfoFound: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val userInfoFound: LiveData<Boolean>
        get() = _userInfoFound

    private var _errorResultMessage: MutableLiveData<String> = MutableLiveData<String>()

    val errorResultMessage: LiveData<String>
        get() = _errorResultMessage

    suspend fun getUserInfo(phoneNumber: String) {
        withContext(Dispatchers.IO){
            val networkUser = userFirestore.getUserInfo(phoneNumber)

            when(networkUser) {
                is Result.Success -> {
                    val userInfo = userNetworkMapper.mapFromEntity(networkUser.data)
                    userDao.saveUserInfo(userCacheMapper.mapToEntity(userInfo))
                    _userInfoFound.postValue(true)
                    Log.d("UserInfoRepository", "User found!")
                }
                is Result.Error -> {
                    val message = networkUser.message
                    if (message == "User not found!"){
                        _errorResultMessage.postValue(message!!)
                        Log.e("UserInfoRepository", "User not found")
                        _userInfoFound.postValue(false)
                    } else {
                        Log.e("UserInfoRepository", message!!)
                    }
                }
            }
        }
    }

    suspend fun saveUserInfo(userNetworkEntity: UserNetworkEntity) {
        userFirestore.saveUserInfo(userNetworkEntity)
    }

}