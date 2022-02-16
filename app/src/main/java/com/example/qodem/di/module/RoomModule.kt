package com.example.qodem.di.module

import android.content.Context
import androidx.room.Room
import com.example.qodem.data.bloodbanks.local.BloodBankDao
import com.example.qodem.data.bloodbanks.local.BloodBankDatabase
import com.example.qodem.data.userinfo.local.UserDao
import com.example.qodem.data.userinfo.local.UserDatabase
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
                BloodBankDatabase.DATABASE_NAME,
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideBloodBankDAO(bloodBankDatabase: BloodBankDatabase): BloodBankDao {
        return bloodBankDatabase.bloodBankDao()
    }

    @Singleton
    @Provides
    fun provideUserDb(@ApplicationContext context: Context): UserDatabase {
        return Room
            .databaseBuilder(
                context,
                UserDatabase::class.java,
                UserDatabase.DATABASE_NAME,
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDAO(userDatabase: UserDatabase): UserDao {
        return userDatabase.userDao()
    }
}