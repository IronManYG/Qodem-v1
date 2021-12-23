package com.example.qodem.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WorkingHours(

    @SerializedName("numberOfHours")
    @Expose
    val numberOfHours: Int,

    @SerializedName("startTime")
    @Expose
    val startTime: String,

    @SerializedName("endTime")
    @Expose
    val endTime: String
)