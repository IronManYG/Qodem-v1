package com.example.qodem.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Coordinates(

    @SerializedName("lat")
    @Expose
    val lat: Double,

    @SerializedName("lng")
    @Expose
    val lng: Double
)