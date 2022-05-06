package com.example.qodem.data.bloodbanks.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BloodBankDao {

    /**
     * @return all blood banks.
     */
    @Query("SELECT * FROM bloodBanks")
    fun getBloodBanks(): Flow<List<BloodBankCacheEntity>>

    /**
     * Insert a blood bank in the database. If the user info already exists, replace it.
     *
     * @param bloodBankEntity the blood bank to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBloodBank(bloodBankEntity: BloodBankCacheEntity)

    /**
     * Clear all bloodBanks form date base.
     *
     * used only when sign out.
     */
    @Query("DELETE FROM bloodBanks")
    suspend fun deleteAllBloodBanks()
}