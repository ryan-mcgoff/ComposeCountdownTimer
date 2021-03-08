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
package com.example.androiddevchallenge.ui.Countdown

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.ExplosionAnimation

@Composable
fun CountDownLayout(
    timeLeft: String? = "0",
    timerFinished: Boolean = true,
    timerList: List<TimerObject>,
    onTimerStart: () -> Unit = {},
    onTimerFinished: () -> Unit = {},
    onInputDone: () -> Unit = {},
    screenWidth: Float
) {
    Surface(color = MaterialTheme.colors.background) {
        Column {
            CountDownRow(onDone = onInputDone, timerList = timerList)
            // TODO: Should use an ImageLoader/provider instead (It's bad practice to directly use Android resources/dependencies in a composable)
            ExplosionAnimation(
                targetImage = painterResource(id = R.drawable.ic_sun),
                movingImage = painterResource(id = R.drawable.ic_android),
                startAnimation = timerFinished,
                screenWidth = screenWidth
            )
            Button(
                onClick = {
                    if (timerFinished) {
                        onTimerStart()
                    } else {
                        onTimerFinished()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = if (timerFinished) "Start Timer" else "Stop Timer")
            }
            Text(
                text = timeLeft ?: "0",
                fontSize = 60.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun CountDownInputField(
    label: String = "",
    maxValue: Int,
    value: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit
) {
    val testValue = remember { mutableStateOf(value) }
    TextField(
        value = testValue.value,
        onValueChange = {
            val newValue = if (it.toIntOrNull() ?: 0 > maxValue) maxValue.toString() else it
            testValue.value = newValue
            onValueChange(newValue)
        },
        singleLine = true,
        label = { Text(text = label) },
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        )
    )
}

@Composable
fun CountDownRow(onDone: () -> Unit = {}, timerList: List<TimerObject>) {
    Row {
        for (timer in timerList) {
            Box(
                Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                CountDownInputField(
                    label = timer.label,
                    maxValue = timer.maxValue,
                    value = timer.value,
                    onValueChange = timer.onValueChange,
                    onDone = { onDone() }
                )
            }
        }
    }
}
