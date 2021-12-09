package com.example.qodem.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface BloodBanksApi {

    @GET("/b/61b1ae6e01558c731cd0e806/2")
    suspend fun getBloodBanks(): Response<List<BloodBank>>

}