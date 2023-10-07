package com.eduardo.bullseye.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.eduardo.bullseye.R

@Composable
fun ResultDialog(
    modifier: Modifier = Modifier,
    dialogTitle : Int,
    sliderValue: Int,
    points: Int,
    onRoundIncrement:() -> Unit,
    hideDialog: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            hideDialog()
            onRoundIncrement()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    hideDialog()
                    onRoundIncrement()
                }
            ) {
                Text(stringResource(id = R.string.resultDialog_btn_text))
            }
        },
        title = { Text(stringResource(id = dialogTitle)) },
        text = { Text(stringResource(id = R.string.resultDialog_message, sliderValue, points)) }
        //text = {Text(text = "The slider's value is $sliderValue")}
    )

}