package com.example.qodem.data.remote

data class BloodBank(
    val bloodDonationCampaign: Boolean,
    val campaignPeriod: Any?,
    val city: String,
    val classification: String,
    val coordinates: Coordinates,
    val donorLimit: Int,
    val gapBetweenAppointment: String,
    val id: Int,
    val name_ar: String,
    val name_en: String,
    val phoneNumber: String?,
    val workingDays: WorkingDays,
    val workingHours: WorkingHours
)