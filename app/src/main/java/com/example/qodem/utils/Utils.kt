package com.example.qodem.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.example.qodem.model.AppointmentDay
import com.example.qodem.model.AppointmentTime
import com.example.qodem.model.BloodBank
import java.util.*

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

fun appointmentDaysList(): List<AppointmentDay> {
    val secondsInMilli: Long = 1000
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60
    val daysInMilli = hoursInMilli * 24
    val currentTime = Calendar.getInstance().timeInMillis
    val daysListAsLong: MutableList<Long> = listOf(currentTime).toMutableList()
    var nextDay = currentTime + daysInMilli
    for (i in 13 downTo 1) {
        daysListAsLong.add(nextDay)
        nextDay += daysInMilli
    }
    val daysList: MutableList<AppointmentDay> =
        daysListAsLong.map { AppointmentDay(it) }.toMutableList()
    return daysList.toList()
}

fun appointmentTimesList(bloodBank: BloodBank): List<AppointmentTime> {
    val secondsInMilli: Long = 1000
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60
    val startTime = bloodBank.workingHours.startTime
    val endTime = bloodBank.workingHours.endTime
    val workingHours = endTime - startTime
    var startTimeInMilli = startTime * hoursInMilli
    val endTimeInMilli = endTime * hoursInMilli
    val workingHoursInMilli = endTimeInMilli - startTimeInMilli
    val timesListAsLong: MutableList<Long> = listOf(startTimeInMilli).toMutableList()
    for (i in (workingHours * 2) downTo 1) {
        startTimeInMilli += hoursInMilli / 2
        timesListAsLong.add(startTimeInMilli)
    }
    val timesList: MutableList<AppointmentTime> =
        timesListAsLong.map { AppointmentTime(it) }.toMutableList()
    return timesList.toList()
}