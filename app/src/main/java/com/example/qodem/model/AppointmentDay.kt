package com.example.qodem.model

// Only for RecyclerView
data class AppointmentDay(
    var dayInMilli: Long = 0L
) {
    // used in recyclerView to if item selected or not
    var isSelected = false
}