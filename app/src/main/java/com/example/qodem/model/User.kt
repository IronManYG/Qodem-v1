package com.example.qodem.model

data class User(
    var id: String,
    var firstName: String,
    var lastName: String,
    var imageName: String?,
    var bloodType: String,
    var birthDate: String,
    var city:String,
    var phoneNumber: String,
    var numberOfDonations: Int,
    var IDType: String,
    var IDNumber: String,
    var communityId: Int?,
    var donations: List<Donation?>
)
