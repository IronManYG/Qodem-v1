package com.example.qodem.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkingHours(

    @SerializedName("startTime")
    @Expose
    val startTime: Int,

    @SerializedName("endTime")
    @Expose
    val endTime: Int
) : Parcelable