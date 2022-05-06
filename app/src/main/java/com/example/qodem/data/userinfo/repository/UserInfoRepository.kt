package com.example.qodem.data.userinfo.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.qodem.data.userinfo.local.DonationsCacheMapper
import com.example.qodem.data.userinfo.local.UserCacheMapper
import com.example.qodem.data.userinfo.local.UserDao
import com.example.qodem.data.userinfo.remote.*
import com.example.qodem.model.Donation
import com.example.qodem.model.User
import com.example.qodem.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
    private var _userInfoFound: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val userInfoFound: LiveData<Boolean>
        get() = _userInfoFound

    private var _userInfoSaved: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val userInfoSaved: LiveData<Boolean>
        get() = _userInfoSaved

    private var _userInfoUpdated: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val userInfoUpdated: LiveData<Boolean>
        get() = _userInfoUpdated

    //
    private var _donationsFound: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val donationsFound: LiveData<Boolean>
        get() = _donationsFound

    private var _activeDonationFound: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val activeDonationFound: LiveData<Boolean>
        get() = _activeDonationFound

    private var _donationSaved: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val donationSaved: LiveData<Boolean>
        get() = _donationSaved

    private var _donationUpdated: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val donationUpdated: LiveData<Boolean>
        get() = _donationUpdated

    //
    private var _errorResultMessage: MutableLiveData<String?> = MutableLiveData<String?>()

    val errorResultMessage: LiveData<String?>
        get() = _errorResultMessage

    private var _saveErrorMessage: MutableLiveData<String?> = MutableLiveData<String?>()

    val saveErrorMessage: LiveData<String?>
        get() = _saveErrorMessage

    private var _updateErrorMessage: MutableLiveData<String?> = MutableLiveData<String?>()

    val updateErrorMessage: LiveData<String?>
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
                    _userInfoFound.postValue(true)
                    Log.d(TAG, "User found!")
                }
                is Result.Error -> {
                    val message = networkUser.message
                    if (message == "User not found!") {
                        _errorResultMessage.postValue(message)
                        Log.e(TAG, "User not found")
                        _userInfoFound.postValue(false)
                    } else {
                        _userInfoFound.postValue(false)
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
                    _userInfoSaved.postValue(true)
                    Log.d(TAG, "User Successful Saved!")
                }
                is Result.Error -> {
                    val message = saveResult.message
                    _saveErrorMessage.postValue(message)
                    Log.d(TAG, message!!)
                    _userInfoSaved.postValue(false)
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
                    _activeDonationFound.postValue(false)
                    for (donation in donations) {
                        if (donation.active) {
                            _activeDonationFound.postValue(true)
                        }
                    }

                    // pay attention: find another way to delete cache data
                    userDao.deleteAllDonations()

                    //
                    userDao.saveDonations(donationsCacheMapper.mapToEntityList(donations))
                    _donationsFound.postValue(true)
                    Log.d(TAG, "Donations found!")
                }
                is Result.Error -> {
                    val message = networkDonations.message
                    if (message == "There is no donations") {
                        _errorResultMessage.postValue(message)
                        Log.e(TAG, "There is no donations")
                        _donationsFound.postValue(false)
                        _activeDonationFound.postValue(false)
                    } else {
                        _donationsFound.postValue(false)
                        _activeDonationFound.postValue(false)
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
                    _donationSaved.postValue(true)
                    Log.d(TAG, "Donation Successful Saved!")
                }
                is Result.Error -> {
                    val message = saveResult.message
                    _saveErrorMessage.postValue(message)
                    Log.d(TAG, message!!)
                    _donationSaved.postValue(false)
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
                    _donationUpdated.postValue(true)
                    Log.d(TAG, "Donation active state Successful Update!")
                }
                is Result.Error -> {
                    val message = updateStatusResult.message
                    _updateErrorMessage.postValue(message)
                    Log.d(TAG, message!!)
                    _donationUpdated.postValue(false)
                }
            }
        }
    }

    suspend fun updateDonationAuthenticatedState(donationID: String, isActive: Boolean) {
        withContext(Dispatchers.IO) {
            val updateStatusResult = userFirestore.updateDonationAuthenticatedState(donationID, isActive)
            when (updateStatusResult) {
                is Result.Success -> {
                    getAllDonations()
                    _donationUpdated.postValue(true)
                    Log.d(TAG, "Donation authenticated state Successful Update!")
                }
                is Result.Error -> {
                    val message = updateStatusResult.message
                    _updateErrorMessage.postValue(message)
                    Log.d(TAG, message!!)
                    _donationUpdated.postValue(false)
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
                    _userInfoUpdated.postValue(true)
                    Log.d(TAG, "User name Successful Update!")
                }
                is Result.Error -> {
                    val message = updateStatusResult.message
                    _updateErrorMessage.postValue(message)
                    Log.d(TAG, message!!)
                    _userInfoUpdated.postValue(false)
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
                    _userInfoUpdated.postValue(true)
                    Log.d(TAG, "User birth Date Successful Update!")
                }
                is Result.Error -> {
                    val message = updateStatusResult.message
                    _updateErrorMessage.postValue(message)
                    Log.d(TAG, message!!)
                    _userInfoUpdated.postValue(false)
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
                    _userInfoUpdated.postValue(true)
                    Log.d(TAG, "User blood type Successful Update!")
                }
                is Result.Error -> {
                    val message = updateStatusResult.message
                    _updateErrorMessage.postValue(message)
                    Log.d(TAG, message!!)
                    _userInfoUpdated.postValue(false)
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
                    _userInfoUpdated.postValue(true)
                    Log.d(TAG, "User gender Successful Update!")
                }
                is Result.Error -> {
                    val message = updateStatusResult.message
                    _updateErrorMessage.postValue(message)
                    Log.d(TAG, message!!)
                    _userInfoUpdated.postValue(false)
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
                    _userInfoUpdated.postValue(true)
                    Log.d(TAG, "User city Successful Update!")
                }
                is Result.Error -> {
                    val message = updateStatusResult.message
                    _updateErrorMessage.postValue(message)
                    Log.d(TAG, message!!)
                    _userInfoUpdated.postValue(false)
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
                    _userInfoUpdated.postValue(true)
                    Log.d(TAG, "User id type id number Successful Update!")
                }
                is Result.Error -> {
                    val message = updateStatusResult.message
                    _updateErrorMessage.postValue(message)
                    Log.d(TAG, message!!)
                    _userInfoUpdated.postValue(false)
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
        _donationUpdated.postValue(false)
    }

}