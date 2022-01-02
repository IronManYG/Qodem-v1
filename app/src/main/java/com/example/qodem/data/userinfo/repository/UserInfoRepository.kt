package com.example.qodem.data.userinfo.repository

import com.example.qodem.data.userinfo.local.UserCacheMapper
import com.example.qodem.data.userinfo.local.UserDao

class UserInfoRepository
constructor(
    private val userDao: UserDao,
    private val userMapper: UserCacheMapper
) {

}