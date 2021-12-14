package com.example.qodem.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BloodBankDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bloodBankEntity: BloodBankCacheEntity): Long

    @Query("SELECT * FROM bloodBanks")
    suspend fun get(): List<BloodBankCacheEntity>
}