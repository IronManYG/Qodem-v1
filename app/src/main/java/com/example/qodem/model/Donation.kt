package com.example.qodem.model

data class Donation(
    var id: Int,
    var bloodBankID: String,
    var donationData: String,
    var donationTime: String,
    var active:Boolean,
    var authenticated: Boolean,
    var timeStamp: Long
)

