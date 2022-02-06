package com.example.qodem.model

data class Donation(
    var id: String,
    var bloodBankID: String,
    var donationData: String,
    var donationTime: String,
    var active:Boolean,
    var authenticated: Boolean,
    var donationDataTimeStamp: Long
)

