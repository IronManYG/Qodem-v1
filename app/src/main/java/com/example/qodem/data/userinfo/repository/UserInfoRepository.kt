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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.qodem.utils.Result

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
    val userInfo : LiveData<User> = Transformations.map(userDao.getUserInfo()){
        userCacheMapper.mapFromEntity(it)
    }

    val donations : LiveData<List<Donation>> = Transformations.map(userDao.getAllDonations()){
        donationsCacheMapper.mapFromEntityList(it)
    }

    val authenticatedDonations : LiveData<List<Donation>> = Transformations.map(userDao.getAuthenticatedDonations(true)){
        donationsCacheMapper.mapFromEntityList(it)
    }

    val activeDonation : LiveData<Donation> = Transformations.map(userDao.getActiveDonation(true)){
        donationsCacheMapper.mapFromEntity(it)
    }

    //
    private var _userInfoFound: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val userInfoFound: LiveData<Boolean>
        get() = _userInfoFound

    private var _userInfoSaved: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val userInfoSaved: LiveData<Boolean>
        get() = _userInfoSaved

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

    //
    private var _errorResultMessage: MutableLiveData<String?> = MutableLiveData<String?>()

    val errorResultMessage: LiveData<String?>
        get() = _errorResultMessage

    private var _saveErrorMessage: MutableLiveData<String?> = MutableLiveData<String?>()

    val saveErrorMessage: LiveData<String?>
        get() = _saveErrorMessage

    suspend fun getUserInfo(phoneNumber: String) {
        withContext(Dispatchers.IO){
            val networkUser = userFirestore.getUserInfo(phoneNumber)
            when(networkUser) {
                is Result.Success -> {
                    val userInfo = userNetworkMapper.mapFromEntity(networkUser.data)
                    userDao.saveUserInfo(userCacheMapper.mapToEntity(userInfo))
                    _userInfoFound.postValue(true)
                    Log.d(TAG, "User found!")
                }
                is Result.Error -> {
                    val message = networkUser.message
                    if (message == "User not found!"){
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
                    for (donation in donations){
                        if (donation.active) {
                            _activeDonationFound.postValue(true)
                        }
                    }
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

}