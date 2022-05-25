package com.example.qodem.data.bloodbanks.remote

import retrofit2.Response

interface BloodBanksDataSource {

    suspend fun getBloodBanks(): Response<List<BloodBankNetworkEntity>>

}