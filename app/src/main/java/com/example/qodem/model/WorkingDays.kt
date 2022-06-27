package com.example.qodem.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkingDays(

    @SerializedName("Saturday")
    @Expose
    val Saturday: Boolean,

    @SerializedName("Sunday")
    @Expose
    val Sunday: Boolean,

    @SerializedName("Monday")
    @Expose
    val Monday: Boolean,

    @SerializedName("Tuesday")
    @Expose
    val Tuesday: Boolean,

    @SerializedName("Wednesday")
    @Expose
    val Wednesday: Boolean,

    @SerializedName("Thursday")
    @Expose
    val Thursday: Boolean,

    @SerializedName("Friday")
    @Expose
    val Friday: Boolean,
) : Parcelable