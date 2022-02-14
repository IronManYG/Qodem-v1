package com.example.qodem.data.userinfo.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.qodem.model.Donation

@Entity(tableName = "User")
class UserCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "firstName")
    var firstName: String,

    @ColumnInfo(name = "lastName")
    var lastName: String,

    @ColumnInfo(name = "imageName")
    var imageName: String?,

    @ColumnInfo(name = "bloodType")
    var bloodType: String,

    @ColumnInfo(name = "birthDate")
    var birthDate: String,

    @ColumnInfo(name = "gender")
    var gender: String,

    @ColumnInfo(name = "city")
    var city:String,

    @ColumnInfo(name = "phoneNumber")
    var phoneNumber: String,

    @ColumnInfo(name = "numberOfDonations")
    var numberOfDonations: Int,

    @ColumnInfo(name = "IDType")
    var IDType: String,

    @ColumnInfo(name = "IDNumber")
    var IDNumber: String,

    @ColumnInfo(name = "communityId")
    var communityId: Int?,

    @ColumnInfo(name = "donations")
    var donations: List<Donation?>

)