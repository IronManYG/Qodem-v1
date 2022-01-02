package com.example.qodem.di.module

import android.content.Context
import androidx.room.Room
import com.example.qodem.data.bloodbanks.local.BloodBankDao
import com.example.qodem.data.bloodbanks.local.BloodBankDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideBloodBankDb(@ApplicationContext context: Context): BloodBankDatabase {
        return Room
            .databaseBuilder(
                context,
                BloodBankDatabase::class.java,
                BloodBankDatabase.DATABASE_NAME,)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideBloodBankDAO(bloodBankDatabase: BloodBankDatabase): BloodBankDao {
        return bloodBankDatabase.bloodBankDao()
    }
}