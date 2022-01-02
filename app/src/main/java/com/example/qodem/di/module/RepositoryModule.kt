package com.example.qodem.di.module

import com.example.qodem.data.bloodbanks.local.BloodBankDao
import com.example.qodem.data.bloodbanks.local.CacheMapper
import com.example.qodem.data.bloodbanks.remote.BloodBanksRetrofit
import com.example.qodem.data.bloodbanks.remote.NetworkMapper
import com.example.qodem.data.bloodbanks.repository.BloodBankRepository
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
    ): BloodBankRepository {
        return BloodBankRepository(bloodBanksDao, retrofit, cacheMapper, networkMapper)
    }
}