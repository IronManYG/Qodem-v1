package com.example.qodem.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class BloodBank(
    var id: Int,
    var name_en: String,
    var name_ar: String,
    var city: String,
    var workingHours: WorkingHours,
    var workingDays: WorkingDays,
    var classification: String,
    var phoneNumber: String?,
    var coordinates: LatLng,
    var gapBetweenAppointment: String,
    var donorLimit: Int,
    var bloodDonationCampaign: Boolean,
    var campaignPeriod: Int?
) : ClusterItem, Parcelable {

    // used in recyclerView to if item selected or not
    var isSelected = false

    override fun getPosition(): LatLng =
        coordinates

    override fun getTitle(): String =
        name_en

    override fun getSnippet(): String =
        name_en

}
