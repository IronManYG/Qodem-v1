package com.example.qodem.model

data class BloodBank(
    var id: Int,
    var name_en: String,
    var name_ar: String,
    var city: String,
    var workingHours: WorkingHours,
    var workingDays: WorkingDays,
    var classification: String,
    var phoneNumber: String?,
    var coordinates: Coordinates,
    var gapBetweenAppointment: String,
    var donorLimit: Int,
    var bloodDonationCampaign: Boolean,
    var campaignPeriod: Int?
)
