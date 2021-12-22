package com.example.qodem.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BloodBankDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bloodBankEntity: BloodBankCacheEntity)

    @Query("SELECT * FROM bloodBanks")
    fun get(): LiveData<List<BloodBankCacheEntity>>
}