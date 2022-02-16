package com.example.qodem.di.module

import com.example.qodem.data.userinfo.remote.UserFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FireStoreModule {

    @Singleton
    @Provides
    fun provideFireStore(): UserFirestore{
        return UserFirestore()
    }

}