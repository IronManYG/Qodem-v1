package com.example.qodem.model

data class Donation(
    var id: String,
    var bloodBankID: String,
    var donationData: String,
    var donationTime: String,
    var authenticated: Boolean,
)

