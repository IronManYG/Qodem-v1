package com.example.qodem.data.bloodbanks.remote

import com.example.qodem.model.Coordinates
import com.example.qodem.model.WorkingDays
import com.example.qodem.model.WorkingHours
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BloodBankNetworkEntity(

    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("name_en")
    @Expose
    val name_en: String,

    @SerializedName("name_ar")
    @Expose
    val name_ar: String,

    @SerializedName("city")
    @Expose
    val city: String,

    @SerializedName("workingHours")
    @Expose
    val workingHours: WorkingHours,

    @SerializedName("workingDays")
    @Expose
    val workingDays: WorkingDays,

    @SerializedName("classification")
    @Expose
    val classification: String,

    @SerializedName("phoneNumber")
    @Expose
    val phoneNumber: String?,

    @SerializedName("coordinates")
    @Expose
    val coordinates: Coordinates,

    @SerializedName("gapBetweenAppointment")
    @Expose
    val gapBetweenAppointment: String,

    @SerializedName("donorLimit")
    @Expose
    val donorLimit: Int,

    @SerializedName("bloodDonationCampaign")
    @Expose
    val bloodDonationCampaign: Boolean,

    @SerializedName("campaignPeriod")
    @Expose
    val campaignPeriod: Int?
)