/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * CountDownLifeCycleObserver provides a countdown to be used in conjunction with a LifeCycle
 */
class CountDownViewModel : ViewModel() {
    // Time
    var timerProperties = MutableLiveData(CountDownTimeProps())

    private val _countDownStatus = MutableLiveData<CountDownStatus>()
    val countDownStatus: LiveData<CountDownStatus> = _countDownStatus

    private val TIMER_INTERVAL_IN_MILLIS = 1000L
    private var countDownTimer: CountDownTimer? = null

    fun startCountdown() {
        // Add interval so countdown starts 1 second after it's been called
        val endTime = (
            timerProperties.value?.generateCountDownEnd()
                ?: Date().time
            ) + TIMER_INTERVAL_IN_MILLIS
        countDownTimer = object : CountDownTimer(endTime - Date().time, TIMER_INTERVAL_IN_MILLIS) {
            override fun onTick(millisUntilFinished: Long) {
                _countDownStatus.postValue(
                    CountDownStatus(
                        timeRemaining = convertTimeToString(endTime - Date().time),
                        isCountDownFinished = false
                    )
                )
            }

            override fun onFinish() {
                stopCountdown()
                _countDownStatus.postValue(
                    CountDownStatus(
                        timeRemaining = "0",
                        isCountDownFinished = true
                    )
                )
            }
        }.apply {
            start()
        }
    }

    fun stopCountdown() {
        countDownTimer?.cancel()
        _countDownStatus.postValue(CountDownStatus(timeRemaining = "0", isCountDownFinished = true))
    }

    private fun CountDownTimeProps.generateCountDownEnd(): Long {
        val currentTime = Date().time
        val ONE_MINUTE_IN_MILLIS: Long = 60000 // millisecs
        val ONE_HOUR_IN_MILLIS: Long = 3600000 // millisecs
        val ONE_SECOND_IN_MILLIS: Long = 1000 // millisecs
        val endTime =
            currentTime + (hoursEntered * ONE_HOUR_IN_MILLIS) + (minutesEntered * ONE_MINUTE_IN_MILLIS) + +(secondsEntered * ONE_SECOND_IN_MILLIS)
        return endTime
    }

    private fun convertTimeToString(remainingTime: Long) =
        TimeUnit.SECONDS.convert(remainingTime, TimeUnit.MILLISECONDS).toString()

    fun getHours() = timerProperties.value?.hoursEntered.toString()
    fun getMinutes() = timerProperties.value?.minutesEntered.toString()
    fun getSeconds() = timerProperties.value?.secondsEntered.toString()
    fun setHours(hours: String) {
        val hoursInt = hours.toIntOrNull() ?: 0
        timerProperties.postValue(timerProperties.value?.copy(hoursEntered = hoursInt))
    }

    fun setSeconds(seconds: String) {
        val secondsInt = seconds.toIntOrNull() ?: 0
        timerProperties.postValue(timerProperties.value?.copy(secondsEntered = secondsInt))
    }

    fun setMinutes(minutes: String) {
        val minutesInt = minutes.toIntOrNull() ?: 0
        timerProperties.postValue(timerProperties.value?.copy(minutesEntered = minutesInt))
    }

    data class CountDownStatus(val timeRemaining: String, val isCountDownFinished: Boolean)
    data class CountDownTimeProps(
        var hoursEntered: Int = 0,
        var minutesEntered: Int = 0,
        var secondsEntered: Int = 0
    )
}
