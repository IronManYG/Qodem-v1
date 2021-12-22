package com.example.qodem.data.remote

import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface BloodBanksRetrofit {

    @GET("/b/61b1ae6e01558c731cd0e806/3")
    suspend fun get(): List<BloodBankNetworkEntity>
}