package com.example.qodem.data.userinfo.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Donations")
class DonationsCacheEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "bloodBankID")
    var bloodBankID: String,

    @ColumnInfo(name = "donationData")
    var donationData: String,

    @ColumnInfo(name = "donationTime")
    var donationTime: String,

    @ColumnInfo(name = "active")
    var active:Boolean,

    @ColumnInfo(name = "authenticated")
    var authenticated: Boolean,

    @ColumnInfo(name = "timeStamp")
    var timeStamp: Long

)