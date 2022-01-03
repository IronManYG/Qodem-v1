package com.example.qodem.data.userinfo.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getUserInfo(): LiveData<UserCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUserInfo(userEntity: UserCacheEntity)
}
