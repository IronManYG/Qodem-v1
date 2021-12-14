package com.example.qodem.data.repository

import com.example.qodem.data.local.BloodBankDao
import com.example.qodem.data.local.CacheMapper
import com.example.qodem.data.remote.BloodBanksRetrofit
import com.example.qodem.data.remote.NetworkMapper
import com.example.qodem.model.BloodBank
import com.example.qodem.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainRepository
constructor(
    private val bloodBankDao: BloodBankDao,
    private val bloodBanksRetrofit: BloodBanksRetrofit,
    private val cacheMapper: CacheMapper,
    private val networkMapper: NetworkMapper
){
    suspend fun getBloodBanks(): Flow<DataState<List<BloodBank>>> = flow {
        emit(DataState.Loading)
        try {
            val networkBloodBanks = bloodBanksRetrofit.get()
            val bloodBanks = networkMapper.mapFromEntityList(networkBloodBanks)
            for(bloodBank in bloodBanks){
                bloodBankDao.insert(cacheMapper.mapToEntity(bloodBank))
            }
            val cachedBloodBanks = bloodBankDao.get()
            emit(DataState.Success(cacheMapper.mapFromEntityList(cachedBloodBanks)))
        }catch (e: Exception){
            emit(DataState.Error(e))
        }
    }
}