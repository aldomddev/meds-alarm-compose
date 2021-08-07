package br.com.amd.medsalarm.ui.widgets

import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import br.com.amd.medsalarm.R
import br.com.amd.medsalarm.databinding.DatePickerBinding

@Composable
fun DateTimePickerDialog(
    showDialog: Boolean = false,
    onDialogClosed: (DialogDismissedState) -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDialogClosed.invoke(DialogDismissedState.DismissRequested) },
            title = { Text(text = stringResource(id = R.string.medication_details_dialog_title)) },
            buttons = {
                DialogButtons(
                    onPositiveButtonClick = { onDialogClosed.invoke(DialogDismissedState.PositiveButtonClicked) },
                    onNegativeButtonClick = { onDialogClosed.invoke(DialogDismissedState.NegativeButtonClicked) }
                )
            },
            text = {
                AndroidViewBinding(DatePickerBinding::inflate) {
                    datePicker.init(2020, 1, 1, ::onDateChanged)
                }
            }
        )
    }
}

@Composable
private fun DialogButtons(
    onPositiveButtonClick: () -> Unit,
    onNegativeButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { onPositiveButtonClick.invoke() }) {
            Text(text = stringResource(id = R.string.medication_details_dialog_positive_button))
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(onClick = { onNegativeButtonClick.invoke() }) {
            Text(text = stringResource(id = R.string.medication_details_dialog_negative_button))
        }
    }
}

sealed class DialogDismissedState {
    object PositiveButtonClicked : DialogDismissedState()
    object NegativeButtonClicked : DialogDismissedState()
    object DismissRequested : DialogDismissedState()
}

private fun onDateChanged(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
    println("AMD - ahaaa")
}