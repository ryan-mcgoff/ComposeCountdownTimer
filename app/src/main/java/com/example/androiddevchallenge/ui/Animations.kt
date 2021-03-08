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
package com.example.androiddevchallenge.ui

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

private enum class ExplosionState {
    START,
    END
}

@Composable
fun ExplosionAnimation(
    targetImage: Painter,
    movingImage: Painter,
    startAnimation: Boolean,
    animationDuration: Int = 5000,
    screenWidth: Float
) {
    var buttonState by remember { mutableStateOf(ExplosionState.START) }
    val transition = updateTransition(targetState = buttonState)
    val animatedDp by transition.animateDp(
        transitionSpec = {
            when (buttonState) {
                ExplosionState.START -> tween(durationMillis = animationDuration)
                ExplosionState.END -> tween(durationMillis = 0)
            }
        }
    ) {
        when (buttonState) {
            ExplosionState.START -> 0.dp
            ExplosionState.END -> screenWidth.dp
        }
    }

    val animatedAlpha by transition.animateFloat(
        transitionSpec = {
            when (buttonState) {
                ExplosionState.START -> tween(durationMillis = animationDuration - 2000)
                ExplosionState.END -> tween(durationMillis = 2000)
            }
        }
    ) {
        when (buttonState) {
            ExplosionState.START -> 1.0f
            ExplosionState.END -> 0.0f
        }
    }

    buttonState = if (startAnimation) ExplosionState.START else ExplosionState.END
    Row {
        Box {
            Image(
                painter = targetImage,
                contentDescription = "static image"
            )
            Image(
                painter = movingImage,
                contentDescription = "moving image",
                modifier = Modifier.padding(start = animatedDp)
            )

            Surface(
                color = Color(0xFFffa500),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(movingImage.intrinsicSize.height.dp)
                    .alpha(animatedAlpha)
            ) {}
        }
    }
}
