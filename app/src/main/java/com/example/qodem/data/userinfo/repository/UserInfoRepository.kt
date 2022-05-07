package com.example.qodem.data.userinfo.repository

import android.util.Log
import com.example.qodem.data.userinfo.local.DonationsCacheMapper
import com.example.qodem.data.userinfo.local.UserCacheMapper
import com.example.qodem.data.userinfo.local.UserDao
import com.example.qodem.data.userinfo.remote.*
import com.example.qodem.model.Donation
import com.example.qodem.model.User
import com.example.qodem.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserInfoRepository
constructor(
    private val userDao: UserDao,
    private val userFirestore: UserFirestore,
    private val userCacheMapper: UserCacheMapper,
    private val userNetworkMapper: UserNetworkMapper,
    private val donationsCacheMapper: DonationsCacheMapper,
    private val donationsNetworkMapper: DonationsNetworkMapper
) {

    companion object {
        const val TAG = "UserInfoRepository"
    }

    //
    val userInfo: Flow<User> = userDao.getUserInfo().map {
        userCacheMapper.mapFromEntity(it)
    }

    val donations: Flow<List<Donation>> = userDao.getAllDonations().map {
        donationsCacheMapper.mapFromEntityList(it)
    }

    val authenticatedDonations: Flow<List<Donation>> =
        userDao.getAuthenticatedDonations(true).map {
            donationsCacheMapper.mapFromEntityList(it)
        }

    val activeDonation: Flow<Donation> = userDao.getActiveDonation(true).map {
        donationsCacheMapper.mapFromEntity(it)
    }

    //
    private var _userInfoFound: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val userInfoFound: StateFlow<Boolean>
        get() = _userInfoFound

    private var _userInfoSaved: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val userInfoSaved: StateFlow<Boolean>
        get() = _userInfoSaved

    private var _userInfoUpdated: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val userInfoUpdated: StateFlow<Boolean>
        get() = _userInfoUpdated

    //
    private var _donationsFound: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val donationsFound: StateFlow<Boolean>
        get() = _donationsFound

    private var _activeDonationFound: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val activeDonationFound: StateFlow<Boolean>
        get() = _activeDonationFound

    private var _donationSaved: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val donationSaved: StateFlow<Boolean>
        get() = _donationSaved

    private var _donationUpdated: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val donationUpdated: StateFlow<Boolean>
        get() = _donationUpdated

    //
    private var _errorResultMessage: MutableStateFlow<String?> = MutableStateFlow("")

    val errorResultMessage: StateFlow<String?>
        get() = _errorResultMessage

    private var _saveErrorMessage: MutableStateFlow<String?> = MutableStateFlow("")

    val saveErrorMessage: StateFlow<String?>
        get() = _saveErrorMessage

    private var _updateErrorMessage: MutableStateFlow<String?> = MutableStateFlow("")

    val updateErrorMessage: StateFlow<String?>
        get() = _updateErrorMessage

    suspend fun getUserInfo(phoneNumber: String) {
        withContext(Dispatchers.IO) {
            val networkUser = userFirestore.getUserInfo(phoneNumber)
            when (networkUser) {
                is Result.Success -> {
                    val userInfo = userNetworkMapper.mapFromEntity(networkUser.data)

                    // pay attention: find another way to delete cache data
                    userDao.deleteUserInfo()

                    userDao.saveUserInfo(userCacheMapper.mapToEntity(userInfo))
                    _userInfoFound.value = true
                    Log.d(TAG, "User found!")
                }
                is Result.Error -> {
                    val message = networkUser.message
                    if (message == "User not found!") {
                        _errorResultMessage.value = message
                        Log.e(TAG, "User not found")
                        _userInfoFound.value = false
                    } else {
                        _userInfoFound.value = false
                        Log.e(TAG, message!!)
                    }
                }
            }
        }
    }

    suspend fun saveUserInfo(userNetworkEntity: UserNetworkEntity) {
        withContext(Dispatchers.IO) {
            val saveResult = userFirestore.saveUserInfo(userNetworkEntity)
            when (saveResult) {
                is Result.Success -> {
                    _userInfoSaved.value = true
                    Log.d(TAG, "User Successful Saved!")
                }
                is Result.Error -> {
                    val message = saveResult.message
                    _saveErrorMessage.value = message
                    Log.d(TAG, message!!)
                    _userInfoSaved.value = false
                }
            }
        }
    }

    suspend fun getAllDonations() {
        withContext(Dispatchers.IO) {
            val networkDonations = userFirestore.getAllDonations()
            when (networkDonations) {
                is Result.Success -> {
                    Log.d(TAG, "Donation 1 id ${networkDonations.data[0].id}")
                    val donations = donationsNetworkMapper.mapFromEntityList(networkDonations.data)
                    //
                    _activeDonationFound.value = false
                    for (donation in donations) {
                        if (donation.active) {
                            _activeDonationFound.value = true
                        }
                    }

                    // pay attention: find another way to delete cache data
                    userDao.deleteAllDonations()

                    //
                    userDao.saveDonations(donationsCacheMapper.mapToEntityList(donations))
                    _donationsFound.value = true
                    Log.d(TAG, "Donations found!")
                }
                is Result.Error -> {
                    val message = networkDonations.message
                    if (message == "There is no donations") {
                        _errorResultMessage.value = message
                        Log.e(TAG, "There is no donations")
                        _donationsFound.value = false
                        _activeDonationFound.value = false
                    } else {
                        _donationsFound.value = false
                        _activeDonationFound.value = false
                        Log.e(TAG, message!!)
                    }
                }
            }
        }
    }

    suspend fun saveDonation(donationNetworkEntity: DonationNetworkEntity) {
        withContext(Dispatchers.IO) {
            val saveResult = userFirestore.saveDonation(donationNetworkEntity)
            when (saveResult) {
                is Result.Success -> {
                    _donationSaved.value = true
                    Log.d(TAG, "Donation Successful Saved!")
                }
                is Result.Error -> {
                    val message = saveResult.message
                    _saveErrorMessage.value = message
                    Log.d(TAG, message!!)
                    _donationSaved.value = false
                }
            }
        }
    }

    suspend fun updateDonationActiveState(donationID: String, isActive: Boolean) {
        withContext(Dispatchers.IO) {
            val updateStatusResult = userFirestore.updateDonationActiveState(donationID, isActive)
            when (updateStatusResult) {
                is Result.Success -> {
                    getAllDonations()
                    _donationUpdated.value = true
                    Log.d(TAG, "Donation active state Successful Update!")
                }
                is Result.Error -> {
                    val message = updateStatusResult.message
                    _updateErrorMessage.value = message
                    Log.d(TAG, message!!)
                    _donationUpdated.value = false
                }
            }
        }
    }

    suspend fun updateDonationAuthenticatedState(donationID: String, isActive: Boolean) {
        withContext(Dispatchers.IO) {
            val updateStatusResult =
                userFirestore.updateDonationAuthenticatedState(donationID, isActive)
            when (updateStatusResult) {
                is Result.Success -> {
                    getAllDonations()
                    _donationUpdated.value = true
                    Log.d(TAG, "Donation authenticated state Successful Update!")
                }
                is Result.Error -> {
                    val message = updateStatusResult.message
                    _updateErrorMessage.value = message
                    Log.d(TAG, message!!)
                    _donationUpdated.value = false
                }
            }
        }
    }

    suspend fun updateUserName(
        userID: String,
        firstName: String,
        lastName: String,
        phoneNumber: String
    ) {
        withContext(Dispatchers.IO) {
            val updateStatusResult = userFirestore.updateUserName(userID, firstName, lastName)
            when (updateStatusResult) {
                is Result.Success -> {
                    getUserInfo(phoneNumber)
                    _userInfoUpdated.value = true
                    Log.d(TAG, "User name Successful Update!")
                }
                is Result.Error -> {
                    val message = updateStatusResult.message
                    _updateErrorMessage.value = message
                    Log.d(TAG, message!!)
                    _userInfoUpdated.value = false
                }
            }
        }
    }

    suspend fun updateUserDateOFBirth(userID: String, dateOfBirth: String, phoneNumber: String) {
        withContext(Dispatchers.IO) {
            val updateStatusResult = userFirestore.updateUserDateOFBirth(userID, dateOfBirth)
            when (updateStatusResult) {
                is Result.Success -> {
                    getUserInfo(phoneNumber)
                    _userInfoUpdated.value = true
                    Log.d(TAG, "User birth Date Successful Update!")
                }
                is Result.Error -> {
                    val message = updateStatusResult.message
                    _updateErrorMessage.value = message
                    Log.d(TAG, message!!)
                    _userInfoUpdated.value = false
                }
            }
        }
    }

    suspend fun updateUserBloodType(userID: String, bloodType: String, phoneNumber: String) {
        withContext(Dispatchers.IO) {
            val updateStatusResult = userFirestore.updateUserBloodType(userID, bloodType)
            when (updateStatusResult) {
                is Result.Success -> {
                    getUserInfo(phoneNumber)
                    _userInfoUpdated.value = true
                    Log.d(TAG, "User blood type Successful Update!")
                }
                is Result.Error -> {
                    val message = updateStatusResult.message
                    _updateErrorMessage.value = message
                    Log.d(TAG, message!!)
                    _userInfoUpdated.value = false
                }
            }
        }
    }

    suspend fun updateUserGender(userID: String, gender: String, phoneNumber: String) {
        withContext(Dispatchers.IO) {
            val updateStatusResult = userFirestore.updateUserGender(userID, gender)
            when (updateStatusResult) {
                is Result.Success -> {
                    getUserInfo(phoneNumber)
                    _userInfoUpdated.value = true
                    Log.d(TAG, "User gender Successful Update!")
                }
                is Result.Error -> {
                    val message = updateStatusResult.message
                    _updateErrorMessage.value = message
                    Log.d(TAG, message!!)
                    _userInfoUpdated.value = false
                }
            }
        }
    }

    suspend fun updateUserCity(userID: String, city: String, phoneNumber: String) {
        withContext(Dispatchers.IO) {
            val updateStatusResult = userFirestore.updateUserCity(userID, city)
            when (updateStatusResult) {
                is Result.Success -> {
                    getUserInfo(phoneNumber)
                    _userInfoUpdated.value = true
                    Log.d(TAG, "User city Successful Update!")
                }
                is Result.Error -> {
                    val message = updateStatusResult.message
                    _updateErrorMessage.value = message
                    Log.d(TAG, message!!)
                    _userInfoUpdated.value = false
                }
            }
        }
    }

    suspend fun updateUserID(
        userID: String,
        idType: String,
        idNumber: String,
        phoneNumber: String
    ) {
        withContext(Dispatchers.IO) {
            val updateStatusResult = userFirestore.updateUserID(userID, idType, idNumber)
            when (updateStatusResult) {
                is Result.Success -> {
                    getUserInfo(phoneNumber)
                    _userInfoUpdated.value = true
                    Log.d(TAG, "User id type id number Successful Update!")
                }
                is Result.Error -> {
                    val message = updateStatusResult.message
                    _updateErrorMessage.value = message
                    Log.d(TAG, message!!)
                    _userInfoUpdated.value = false
                }
            }
        }
    }

    // pay attention: find another way to delete cache data
    suspend fun clearUserInfo() {
        withContext(Dispatchers.IO) {
            userDao.deleteUserInfo()
            userDao.deleteAllDonations()
        }
    }

    // pay attention: find another way to delete cache data
    fun resetDonationUpdatedState() {
        _donationUpdated.value = false
    }

}