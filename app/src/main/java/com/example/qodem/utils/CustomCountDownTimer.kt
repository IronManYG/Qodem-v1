package com.example.qodem.utils

import android.os.CountDownTimer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class CustomCountDownTimer(endData: Long) {

    companion object {
        const val TAG = "CustomCountDownTimer"
    }

    //
    private var _countDownDays: MutableStateFlow<String> = MutableStateFlow("0")

    val countDownDays: StateFlow<String>
        get() = _countDownDays

    private var _countDownHours: MutableStateFlow<String> = MutableStateFlow("0")

    val countDownHours: StateFlow<String>
        get() = _countDownHours

    private var _countDownMinutes: MutableStateFlow<String> = MutableStateFlow("0")

    val countDownMinutes: StateFlow<String>
        get() = _countDownMinutes

    private var _countDownSeconds: MutableStateFlow<String> = MutableStateFlow("0")

    val countDownSeconds: StateFlow<String>
        get() = _countDownSeconds

    //
    private lateinit var timer: CountDownTimer

    private val currentTime = Calendar.getInstance().time

    private var different = endData - currentTime.time

    fun start() {
        if (this::timer.isInitialized) {
            return
        }
        timer = object : CountDownTimer(different, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                var diff = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60
                val daysInMilli = hoursInMilli * 24

                val elapsedDays = diff / daysInMilli
                diff %= daysInMilli
                _countDownDays.value = elapsedDays.toString()

                val elapsedHours = diff / hoursInMilli
                diff %= hoursInMilli
                _countDownHours.value = elapsedHours.toString()

                val elapsedMinutes = diff / minutesInMilli
                diff %= minutesInMilli
                _countDownMinutes.value = elapsedMinutes.toString()

                val elapsedSeconds = diff / secondsInMilli
                _countDownSeconds.value = elapsedSeconds.toString()
            }

            override fun onFinish() {

            }
        }
        timer.start()
    }
}