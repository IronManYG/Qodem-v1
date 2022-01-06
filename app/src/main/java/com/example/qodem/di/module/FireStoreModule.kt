package com.example.qodem.di.module

import com.example.qodem.data.userinfo.remote.UserFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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