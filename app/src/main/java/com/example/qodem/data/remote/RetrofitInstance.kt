package com.example.qodem.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val api: BloodBanksApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.jsonbin.io")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(BloodBanksApi::class.java)
    }

}