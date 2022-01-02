package com.example.qodem.data.userinfo.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userEntity: UserCacheEntity)

    @Query("SELECT * FROM user")
    fun get(): LiveData<UserCacheEntity>
}