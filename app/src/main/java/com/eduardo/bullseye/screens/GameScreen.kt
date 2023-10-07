package com.eduardo.bullseye.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eduardo.bullseye.R
import com.eduardo.bullseye.components.GameDetail
import com.eduardo.bullseye.components.GamePrompt
import com.eduardo.bullseye.components.ResultDialog
import com.eduardo.bullseye.components.TargetSlider
import com.eduardo.bullseye.ui.theme.BullseyeTheme
import kotlin.math.abs
import kotlin.random.Random

@Composable
fun GameScreen(
    onNavigateAbout: () -> Unit
) {
    fun newTargetValue(): Int {
        return Random.nextInt(1, 100)
    }

    var alertIsVisible by rememberSaveable { mutableStateOf(false) }
    var sliderValue by rememberSaveable { mutableStateOf(0.5f) }
    var targetValue by rememberSaveable { mutableStateOf(newTargetValue()) }
    val sliderToInt = (sliderValue * 100).toInt()
    var totalScore by rememberSaveable { mutableStateOf(0) }
    var round by rememberSaveable {
        mutableStateOf(1)
    }

    fun differenceAmount(): Int {
        return abs(targetValue - sliderToInt)
    }

    fun scorePoints(): Int {
        val maxScore = 100
        val difference = differenceAmount()
        var bonus = 0
        if (difference == 0) {
            bonus = 100
        } else if (difference == 1) {
            bonus = 50
        }
        return (maxScore - difference) + bonus
    }

    fun startNewGame() {
        totalScore = 0
        round = 1
        targetValue = newTargetValue()
        sliderValue = 0.5f
    }

    fun alertTitle(): Int {
        val difference = differenceAmount()
        val title: Int = if (difference == 0) {
            R.string.alert_title1
        } else if (difference < 5) {
            R.string.alert_title2
        } else if (difference <= 10) {
            R.string.alert_title3
        } else {
            R.string.alert_title4
        }
        return title
    }

    Box {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.background ),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(R.string.background_image)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.weight(.5f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(9f)
            ) {
                GamePrompt(targetValue = targetValue)
                TargetSlider(value = sliderValue, valueChanged = { value ->
                    sliderValue = value
                })
                Button(
                    onClick = {
                        alertIsVisible = true
                        totalScore += scorePoints()

                    },
                    shape = shapes.medium,
                    contentPadding = PaddingValues(16.dp)
                )
                {
                    Text(text = stringResource(R.string.btn_hit))
                }
                GameDetail(
                    round = round,
                    totalScore = totalScore,
                    modifier = Modifier.fillMaxWidth(),
                    onStartOver = { startNewGame() },
                    onNavigateAbout = onNavigateAbout
                )
            }
            Spacer(modifier = Modifier.weight(.5f))

            if (alertIsVisible) {
                ResultDialog(dialogTitle = alertTitle(),
                    hideDialog = { alertIsVisible = false },
                    sliderValue = sliderToInt,
                    points = scorePoints(),
                    onRoundIncrement = {
                        round += 1
                        targetValue = newTargetValue()
                    })
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.AUTOMOTIVE_1024p, widthDp = 864, heightDp = 432)
@Composable
fun GameScreenPreview() {
    BullseyeTheme {
        GameScreen(onNavigateAbout = {})
    }
}
