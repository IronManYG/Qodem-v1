package com.example.qodem.data.userinfo.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the user info & donations tables.
 */
@Dao
interface UserDao {

    /**
     * @return user info.
     */
    @Query("SELECT * FROM user")
    fun getUserInfo(): Flow<UserCacheEntity>

    /**
     * Insert a user info in the database. If the user info already exists, replace it.
     *
     * @param userEntity the user info to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserInfo(userEntity: UserCacheEntity)

    /**
     * @return all donations.
     */
    @Query("SELECT * FROM donations")
    fun getAllDonations(): Flow<List<DonationsCacheEntity>>

    /**
     * @return all authenticated or not authenticated donation in the database.
     *
     * @param isAuthenticated the state of donation to be return.
     */
    @Query("SELECT * FROM donations WHERE authenticated = :isAuthenticated")
    fun getAuthenticatedDonations(isAuthenticated: Boolean): Flow<List<DonationsCacheEntity>>

    /**
     * Insert donations in the database. If the donations already exists, replace it.
     *
     * @param donationsEntity are donations to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDonations(donationsEntity: List<DonationsCacheEntity>)

    /**
     * @return active or inactive donation in the database.
     *
     * @param isActive the state of donation to be inserted.
     */
    @Query("SELECT *  FROM donations WHERE active = :isActive")
    fun getActiveDonation(isActive: Boolean): Flow<DonationsCacheEntity>

    /**
     * Clear user info form date base.
     *
     * used only when sign out.
     */
    @Query("DELETE FROM user")
    suspend fun deleteUserInfo()

    /**
     * Clear all donations form date base.
     *
     * used only when sign out.
     */
    @Query("DELETE FROM donations")
    suspend fun deleteAllDonations()
}
