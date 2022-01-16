package com.example.qodem.data.userinfo.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Data Access Object for the user info & donations tables.
 */
@Dao
interface UserDao {

    /**
     * @return user info.
     */
    @Query("SELECT * FROM user")
    fun getUserInfo(): LiveData<UserCacheEntity>

    /**
     * Insert a user info in the database. If the user info already exists, replace it.
     *
     * @param userEntity the user info to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUserInfo(userEntity: UserCacheEntity)

    /**
     * @return all donations.
     */
    @Query("SELECT * FROM donations")
    fun getAllDonations(): LiveData<List<DonationsCacheEntity>>

    /**
     * @return all authenticated or not authenticated donation in the database.
     *
     * @param isAuthenticated the state of donation to be return.
     */
    @Query("SELECT * FROM donations WHERE authenticated = :isAuthenticated")
    fun getDonations(isAuthenticated: Boolean): LiveData<List<DonationsCacheEntity>>

    /**
     * Insert donations in the database. If the donations already exists, replace it.
     *
     * @param donationsEntity are donations to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveDonations(donationsEntity: DonationsCacheEntity)

    /**
     * @return active or inactive donation in the database.
     *
     * @param active the state of donation to be inserted.
     */
    @Query("SELECT *  FROM donations WHERE active = :isActive")
    fun getDonation(isActive: Boolean): LiveData<DonationsCacheEntity>
}
