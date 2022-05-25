package com.example.qodem.data.bloodbanks.remote

import retrofit2.Response
import retrofit2.http.GET

interface BloodBanksRetrofit : BloodBanksDataSource {

    /**
     * @return all blood banks.
     */
    @GET("/b/61b1ae6e01558c731cd0e806/4")
    override suspend fun getBloodBanks(): Response<List<BloodBankNetworkEntity>>
}