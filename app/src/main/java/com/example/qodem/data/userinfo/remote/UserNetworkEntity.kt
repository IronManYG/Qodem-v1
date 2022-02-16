package com.example.qodem.data.userinfo.remote

import com.example.qodem.model.Donation

class UserNetworkEntity(

    var id: String = "",

    var firstName: String = "",

    var lastName: String = "",

    var imageName: String? = "",

    var bloodType: String = "",

    var birthDate: String = "",

    var gender: String = "",

    var city: String = "",

    var phoneNumber: String = "",

    var numberOfDonations: Int = -1,

    var IDType: String = "",

    var IDNumber: String = "",

    var communityId: Int? = -1,

    var donations: List<Donation?> = emptyList()
)