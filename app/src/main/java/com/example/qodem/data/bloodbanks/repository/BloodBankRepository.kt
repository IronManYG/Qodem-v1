package com.example.qodem.data.bloodbanks.repository

import android.util.Log
import com.example.qodem.data.bloodbanks.local.BloodBankCacheMapper
import com.example.qodem.data.bloodbanks.local.BloodBankDao
import com.example.qodem.data.bloodbanks.remote.BloodBankNetworkMapper
import com.example.qodem.data.bloodbanks.remote.BloodBanksRetrofit
import com.example.qodem.model.BloodBank
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
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

    val bloodBanks: Flow<List<BloodBank>> = bloodBankDao.getBloodBanks().map {
        bloodBankCacheMapper.mapFromEntityList(it)
    }

    private var _bloodBanksFound: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val bloodBanksFound: StateFlow<Boolean>
        get() = _bloodBanksFound

    //
    private var _errorResultMessage: MutableStateFlow<String?> = MutableStateFlow("")
    val errorResultMessage: StateFlow<String?>
        get() = _errorResultMessage

    suspend fun getBloodBanks() {
        withContext(Dispatchers.IO) {
            val networkBloodBanksResponse = bloodBanksRetrofit.getBloodBanks()
            if (networkBloodBanksResponse.isSuccessful && networkBloodBanksResponse.body() != null) {
                val bloodBanks =
                    bloodBankNetworkMapper.mapFromEntityList(networkBloodBanksResponse.body()!!)
                for (bloodBank in bloodBanks) {
                    bloodBankDao.saveBloodBank(bloodBankCacheMapper.mapToEntity(bloodBank))
                }
                _bloodBanksFound.value = true
                Log.d(TAG, "Blood Banks found!")
            } else {
                val message = networkBloodBanksResponse.message()
                _errorResultMessage.value = message
                Log.e(TAG, message)
                _bloodBanksFound.value = false
                Log.e(TAG, "Response not successful")
            }
        }
    }

    suspend fun clearBloodBanks() {
        withContext(Dispatchers.IO) {
            bloodBankDao.deleteAllBloodBanks()
        }
    }
}