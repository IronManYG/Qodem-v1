package com.example.qodem.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WorkingHours(

    @SerializedName("startTime")
    @Expose
    val startTime: Int,

    @SerializedName("endTime")
    @Expose
    val endTime: Int
)