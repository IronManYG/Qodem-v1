package com.example.qodem.di.module

import com.example.qodem.data.local.BloodBankDao
import com.example.qodem.data.local.CacheMapper
import com.example.qodem.data.remote.BloodBanksRetrofit
import com.example.qodem.data.remote.NetworkMapper
import com.example.qodem.data.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        bloodBanksDao: BloodBankDao,
        retrofit: BloodBanksRetrofit,
        cacheMapper: CacheMapper,
        networkMapper: NetworkMapper
    ): MainRepository {
        return MainRepository(bloodBanksDao, retrofit, cacheMapper, networkMapper)
    }
}