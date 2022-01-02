package com.example.qodem.data.bloodbanks.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.qodem.model.Coordinates
import com.example.qodem.model.WorkingDays
import com.example.qodem.model.WorkingHours

@Entity(tableName = "bloodBanks")
class BloodBankCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "name_en")
    var name_en: String,

    @ColumnInfo(name = "name_ar")
    var name_ar: String,

    @ColumnInfo(name = "city")
    var city: String,

    @ColumnInfo(name = "workingHours")
    var workingHours: WorkingHours,

    @ColumnInfo(name = "workingDays")
    var workingDays: WorkingDays,

    @ColumnInfo(name = "classification")
    var classification: String,

    @ColumnInfo(name = "phoneNumber")
    var phoneNumber: String?,

    @ColumnInfo(name = "coordinates")
    var coordinates: Coordinates,

    @ColumnInfo(name = "gapBetweenAppointment")
    var gapBetweenAppointment: String,

    @ColumnInfo(name = "donorLimit")
    var donorLimit: Int,

    @ColumnInfo(name = "bloodDonationCampaign")
    var bloodDonationCampaign: Boolean,

    @ColumnInfo(name = "campaignPeriod")
    var campaignPeriod: Int?
)