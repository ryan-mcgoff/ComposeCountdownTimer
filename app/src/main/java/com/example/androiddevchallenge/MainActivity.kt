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

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.androiddevchallenge.ui.Countdown.CountDownLayout
import com.example.androiddevchallenge.ui.Countdown.TimerObject
import com.example.androiddevchallenge.ui.theme.MyTheme

@ExperimentalAnimationApi
class MainActivity : AppCompatActivity(), LifecycleOwner {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val countDownTimerViewModel = ViewModelProvider(this).get(CountDownViewModel::class.java)
        setContent {
            MyTheme() {
                CountDownActivityScreen(countDownTimerViewModel = countDownTimerViewModel)
            }
        }
    }

    @Composable
    private fun CountDownActivityScreen(countDownTimerViewModel: CountDownViewModel) {
        // List of Timers
        val timers = remember {
            mutableStateOf(
                listOf(
                    TimerObject(
                        label = "Hours",
                        maxValue = 99,
                        value = countDownTimerViewModel.getHours(),
                        onValueChange = countDownTimerViewModel::setHours
                    ),
                    TimerObject(
                        label = "Minutes",
                        maxValue = 59,
                        value = countDownTimerViewModel.getMinutes(),
                        onValueChange = countDownTimerViewModel::setMinutes
                    ),
                    TimerObject(
                        label = "Seconds",
                        maxValue = 59,
                        value = countDownTimerViewModel.getSeconds(),
                        onValueChange = countDownTimerViewModel::setSeconds
                    )
                )
            )
        }

        // Timer Status State
        val timerStatus by countDownTimerViewModel.countDownStatus.observeAsState()

        CountDownLayout(
            timerList = timers.value,
            timeLeft = timerStatus?.timeRemaining,
            timerFinished = timerStatus?.isCountDownFinished ?: true,
            onTimerStart = { countDownTimerViewModel.startCountdown() },
            onTimerFinished = { countDownTimerViewModel.stopCountdown() },
            onInputDone = { currentFocus?.clearFocus() },
            screenWidth = getScreenWidth()
        )
    }

    private fun getScreenWidth(): Float {
        val displayMetrics: DisplayMetrics = resources.displayMetrics
        return displayMetrics.widthPixels / displayMetrics.density
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        val timerListObjects = listOf(
            TimerObject(
                label = "Hours"
            ),
            TimerObject(
                label = "Minutes"
            ),
            TimerObject(
                label = "Seconds"
            )
        )
        MyTheme {
            CountDownLayout(timerList = timerListObjects, screenWidth = 0.0f)
        }
    }
}
