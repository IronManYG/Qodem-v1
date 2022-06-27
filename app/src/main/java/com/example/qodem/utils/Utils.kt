package com.example.qodem.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.example.qodem.model.BloodBank

fun dialIntent(phoneNumber: String?, context: Context) {
    val dialIntent = Intent(Intent.ACTION_DIAL)
    dialIntent.data = Uri.parse("tel:$phoneNumber")
    startActivity(context, dialIntent, null)
}

fun mapIntent(bloodBank: BloodBank, context: Context) {
    val gmmIntentUri = Uri.parse(
        "geo:0,0?q=" +
                "${bloodBank.coordinates.latitude}," +
                "${bloodBank.coordinates.longitude}" +
                "(${bloodBank.name_en})"
    )
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    mapIntent.resolveActivity(context.packageManager)?.let {
        startActivity(context, mapIntent, null)
    }
}