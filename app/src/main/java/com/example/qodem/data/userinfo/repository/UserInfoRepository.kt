package com.example.qodem.data.userinfo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.qodem.data.userinfo.local.UserCacheMapper
import com.example.qodem.data.userinfo.local.UserDao
import com.example.qodem.data.userinfo.remote.UserFirestore
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

    suspend fun getUserInfo(phoneNumber: String) {
        withContext(Dispatchers.IO){
            val networkUser = userFirestore.getUserInfo(phoneNumber) as Result.Success
            val userInfo = userNetworkMapper.mapFromEntity(networkUser.data)
            userDao.saveUserInfo(userCacheMapper.mapToEntity(userInfo))
        }
    }

}