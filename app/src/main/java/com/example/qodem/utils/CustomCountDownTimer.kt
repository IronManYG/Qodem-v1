package com.example.qodem.utils

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class CustomCountDownTimer(endData: Long) {

    companion object {
        const val TAG = "CustomCountDownTimer"
    }

    //
    private var _countDownDays: MutableLiveData<String> = MutableLiveData<String>()

    val countDownDays: LiveData<String>
        get() = _countDownDays

    private var _countDownHours: MutableLiveData<String> = MutableLiveData<String>()

    val countDownHours: LiveData<String>
        get() = _countDownHours

    private var _countDownMinutes: MutableLiveData<String> = MutableLiveData<String>()

    val countDownMinutes: LiveData<String>
        get() = _countDownMinutes

    private var _countDownSeconds: MutableLiveData<String> = MutableLiveData<String>()

    val countDownSeconds: LiveData<String>
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
                _countDownDays.postValue(elapsedDays.toString())

                val elapsedHours = diff / hoursInMilli
                diff %= hoursInMilli
                _countDownHours.postValue(elapsedHours.toString())

                val elapsedMinutes = diff / minutesInMilli
                diff %= minutesInMilli
                _countDownMinutes.postValue(elapsedMinutes.toString())

                val elapsedSeconds = diff / secondsInMilli
                _countDownSeconds.postValue(elapsedSeconds.toString())
            }

            override fun onFinish() {

            }
        }
        timer.start()
    }
}