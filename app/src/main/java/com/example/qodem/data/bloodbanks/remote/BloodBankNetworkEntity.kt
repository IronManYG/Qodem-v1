package com.example.qodem.data.bloodbanks.remote

import com.example.qodem.model.Coordinates
import com.example.qodem.model.WorkingDays
import com.example.qodem.model.WorkingHours

class BloodBankNetworkEntity(
    val id: Int,
    val name_en: String,
    val name_ar: String,
    val city: String,
    val workingHours: WorkingHours,
    val workingDays: WorkingDays,
    val classification: String,
    val phoneNumber: String?,
    val coordinates: Coordinates,
    val gapBetweenAppointment: String,
    val donorLimit: Int,
    val bloodDonationCampaign: Boolean,
    val campaignPeriod: Int?
)