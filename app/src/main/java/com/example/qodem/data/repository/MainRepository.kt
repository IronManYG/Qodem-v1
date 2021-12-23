package com.example.qodem.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.qodem.data.local.BloodBankDao
import com.example.qodem.data.local.CacheMapper
import com.example.qodem.data.remote.BloodBanksRetrofit
import com.example.qodem.data.remote.NetworkMapper
import com.example.qodem.model.BloodBank
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository
constructor(
    private val bloodBankDao: BloodBankDao,
    private val bloodBanksRetrofit: BloodBanksRetrofit,
    private val cacheMapper: CacheMapper,
    private val networkMapper: NetworkMapper
){

    val bloodBanks : LiveData<List<BloodBank>> = Transformations.map(bloodBankDao.get()){
        cacheMapper.mapFromEntityList(it)
    }

    suspend fun getBloodBanks() {
        withContext(Dispatchers.IO){
            val networkBloodBanks = bloodBanksRetrofit.get()
            val bloodBanks = networkMapper.mapFromEntityList(networkBloodBanks)
            for(bloodBank in bloodBanks){
                bloodBankDao.insert(cacheMapper.mapToEntity(bloodBank))
                Log.d("MainRepository", "workStart")
            }
        }
    }
}