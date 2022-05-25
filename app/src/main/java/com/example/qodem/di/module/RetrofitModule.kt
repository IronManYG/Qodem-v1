package com.example.qodem.di.module

import com.example.qodem.data.bloodbanks.remote.BloodBanksDataSource
import com.example.qodem.data.bloodbanks.remote.BloodBanksRetrofit
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideMoshiBuilder(): Moshi {
        return Moshi
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("https://api.jsonbin.io")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
    }

    @Singleton
    @Provides
    fun provideBloodBankService(retrofit: Retrofit.Builder): BloodBanksDataSource {
        return retrofit
            .build()
            .create(BloodBanksRetrofit::class.java)
    }
}