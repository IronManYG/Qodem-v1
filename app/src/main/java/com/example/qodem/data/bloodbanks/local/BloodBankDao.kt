package com.example.qodem.data.bloodbanks.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BloodBankDao {

    /**
     * @return all blood banks.
     */
    @Query("SELECT * FROM bloodBanks")
    fun getBloodBanks(): LiveData<List<BloodBankCacheEntity>>

    /**
     * Insert a blood bank in the database. If the user info already exists, replace it.
     *
     * @param bloodBankEntity the blood bank to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveBloodBank(bloodBankEntity: BloodBankCacheEntity)
}