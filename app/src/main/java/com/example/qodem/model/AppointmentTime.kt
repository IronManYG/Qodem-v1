package com.example.qodem.model

// Only for RecyclerView
data class AppointmentTime(
    var timeInMilli: Long = 0L
) {
    // used in recyclerView to if item selected or not
    var isSelected = false
}