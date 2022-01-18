package com.example.qodem.data.bloodbanks.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.qodem.data.bloodbanks.local.BloodBankCacheMapper
import com.example.qodem.data.bloodbanks.local.BloodBankDao
import com.example.qodem.data.bloodbanks.remote.BloodBankNetworkMapper
import com.example.qodem.data.bloodbanks.remote.BloodBanksRetrofit
import com.example.qodem.model.BloodBank
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BloodBankRepository
constructor(
    private val bloodBankDao: BloodBankDao,
    private val bloodBanksRetrofit: BloodBanksRetrofit,
    private val bloodBankCacheMapper: BloodBankCacheMapper,
    private val bloodBankNetworkMapper: BloodBankNetworkMapper
) {
    companion object {
        const val TAG = "BloodBankRepository"
    }

    //
    val bloodBanks: LiveData<List<BloodBank>> = Transformations.map(bloodBankDao.getBloodBanks()) {
        bloodBankCacheMapper.mapFromEntityList(it)
    }

    //
    private var _bloodBanksFound: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val bloodBanksFound: LiveData<Boolean>
        get() = _bloodBanksFound

    //
    private var _errorResultMessage: MutableLiveData<String?> = MutableLiveData<String?>()

    val errorResultMessage: LiveData<String?>
        get() = _errorResultMessage

    suspend fun getBloodBanks() {
        withContext(Dispatchers.IO) {
            val networkBloodBanksResponse = bloodBanksRetrofit.getBloodBanks()
            if (networkBloodBanksResponse.isSuccessful && networkBloodBanksResponse.body() != null) {
                val bloodBanks = bloodBankNetworkMapper.mapFromEntityList(networkBloodBanksResponse.body()!!)
                for (bloodBank in bloodBanks) {
                    bloodBankDao.saveBloodBank(bloodBankCacheMapper.mapToEntity(bloodBank))
                }
                _bloodBanksFound.postValue(true)
                Log.d(TAG, "Blood Banks found!")
            } else {
                val message = networkBloodBanksResponse.message()
                _errorResultMessage.postValue(message)
                Log.e(TAG, message)
                _bloodBanksFound.postValue(false)
                Log.e(TAG, "Response not successful")
            }
        }
    }
}