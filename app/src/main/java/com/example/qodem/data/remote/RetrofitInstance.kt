package com.example.qodem.data.remote

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {

    val RETROFIT: BloodBanksRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.jsonbin.io")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(BloodBanksRetrofit::class.java)
    }

}