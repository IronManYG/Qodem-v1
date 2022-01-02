package com.example.qodem.data.bloodbanks.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.qodem.data.bloodbanks.local.BloodBankDao
import com.example.qodem.data.bloodbanks.local.BloodBankCacheMapper
import com.example.qodem.data.bloodbanks.remote.BloodBanksRetrofit
import com.example.qodem.data.bloodbanks.remote.BloodBankNetworkMapper
import com.example.qodem.model.BloodBank
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BloodBankRepository
constructor(
    private val bloodBankDao: BloodBankDao,
    private val bloodBanksRetrofit: BloodBanksRetrofit,
    private val bloodBankCacheMapper: BloodBankCacheMapper,
    private val bloodBankNetworkMapper: BloodBankNetworkMapper
){

    val bloodBanks : LiveData<List<BloodBank>> = Transformations.map(bloodBankDao.get()){
        bloodBankCacheMapper.mapFromEntityList(it)
    }

    suspend fun getBloodBanks() {
        withContext(Dispatchers.IO){
            val networkBloodBanks = bloodBanksRetrofit.get()
            val bloodBanks = bloodBankNetworkMapper.mapFromEntityList(networkBloodBanks)
            for(bloodBank in bloodBanks){
                bloodBankDao.insert(bloodBankCacheMapper.mapToEntity(bloodBank))
                Log.d("MainRepository", "workStart")
            }
        }
    }
}