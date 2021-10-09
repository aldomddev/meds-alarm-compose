package br.com.amd.medsalarm.presentation.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.amd.medsalarm.R
import br.com.amd.medsalarm.presentation.model.RepeatingIntervalVO
import br.com.amd.medsalarm.presentation.viewmodels.MedicationDetailViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@Composable
fun MedicationDetailScreen(
    viewModel: MedicationDetailViewModel,
    onSaveClicked: () -> Unit,
    onCancelClicked: () -> Unit,
) {
    val medication by viewModel.medicationText
    val description by viewModel.descriptionText

    val startsOn by viewModel.startsOnDateTimeStr
    val endsOn by viewModel.endsOnDateTimeStr

    val focusRequester = remember { FocusRequester() }
    val permanent by viewModel.endsOnDateTimeEnabled

    val repeatingSelection by viewModel.repeatingInterval
    val customRepeatingInterval by viewModel.customRepeatingInterval

    val alarmSaved by viewModel.alarmSaved
    if (alarmSaved) {
        onSaveClicked()
    }

    val scrollState = rememberScrollState()

    DatePickerDialog(viewModel = viewModel)
    TimePickerDialog(viewModel = viewModel)

    Column(modifier = Modifier
        .padding(top = 16.dp, bottom = 80.dp, start = 16.dp, end = 16.dp)
        .fillMaxWidth()
        .verticalScroll(scrollState)) {

        // medication
        OutlinedTextField(
            value = medication,
            onValueChange = { viewModel.onMedicationTextChange(text = it) },
            singleLine = true,
            label = { Text(stringResource(id = R.string.medication_details_medication)) },
            placeholder = { Text(text = stringResource(id = R.string.medication_details_medication_placeholder)) },
            modifier = Modifier.fillMaxWidth()
        )

        // description
        OutlinedTextField(
            value = description,
            onValueChange = { viewModel.onDescriptionTextChange(text = it) },
            singleLine = true,
            label = { Text(stringResource(id = R.string.medication_details_description)) },
            placeholder = { Text(text = stringResource(id = R.string.medication_details_description_placeholder)) },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        )

        // starts on
        OutlinedTextField(
            value = startsOn,
            onValueChange = { },
            readOnly = true,
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.medication_details_start_on)) },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { viewModel.onStartsOnFocusChange(focused = it.hasFocus) }
                .focusTarget()
                .pointerInput(Unit) { detectTapGestures { focusRequester.requestFocus() } }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = permanent,
                onCheckedChange = { viewModel.onPermanentCheckBoxClick() }
            )

            Text(text = "Permanente")
        }

        // ends on
        if (!permanent) {
            OutlinedTextField(
                value = endsOn,
                onValueChange = { },
                readOnly = true,
                singleLine = true,
                label = { Text(text = stringResource(id = R.string.medication_details_ends_on)) },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged { viewModel.onEndsOnFocusChange(focused = it.hasFocus) }
                    .focusTarget()
                    .pointerInput(Unit) { detectTapGestures { focusRequester.requestFocus() } }
            )
        }

        RepeatingIntervalRadioGroup(
            items = listOf(
                RepeatingIntervalVO.FOUR,
                RepeatingIntervalVO.SIX,
                RepeatingIntervalVO.EIGHT,
                RepeatingIntervalVO.TWELVE,
                RepeatingIntervalVO.CUSTOM,
            ),
            selectedItem = repeatingSelection,
            onSelectionChanged = { selection -> viewModel.onRepeatingIntervalChanged(selection) }
        )

        CustomRepeatingInterval(
            isVisible = repeatingSelection == RepeatingIntervalVO.CUSTOM,
            value = customRepeatingInterval,
            onValueChange = { viewModel.onCustomRepeatingIntervalChange(it) }
        )

        // save
        Button(
            modifier = Modifier
                .padding(top = 48.dp)
                .fillMaxWidth(),
            onClick = { viewModel.onSaveButtonClick() }
        ) {
            Text("Salvar")
        }

        // cancel
        OutlinedButton(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            onClick = { onCancelClicked() }
        ) {
            Text("Cancelar")
        }
    }
}

@Composable
private fun RepeatingIntervalRadioGroup(
    items: List<RepeatingIntervalVO>,
    selectedItem: RepeatingIntervalVO,
    onSelectionChanged: (RepeatingIntervalVO) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
        ) {
        Text(text = "Repetir a cada (horas)")

        Row(
            modifier = Modifier.padding(top = 1.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            items.forEach { item ->
                RadioButton(
                    selected = item == selectedItem,
                    onClick = { onSelectionChanged.invoke(item) }
                )
                Text(text = stringResource(id = item.intervalRes))
            }
        }
    }
}

@Composable
private fun CustomRepeatingInterval(
    isVisible: Boolean,
    value: String,
    onValueChange: (String) -> Unit
) {
    if (isVisible) {
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            TextField(
                value = value,
                modifier = Modifier.width(130.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                trailingIcon = {
                    Image(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { onValueChange(value.increment()) },
                        painter = painterResource(id = R.drawable.ic_plus),
                        colorFilter = ColorFilter.tint(Color.Black),
                        contentDescription = ""
                    )
                },
                leadingIcon = {
                    Image(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { onValueChange(value.decrement()) },
                        painter = painterResource(id = R.drawable.ic_minus),
                        colorFilter = ColorFilter.tint(Color.Black),
                        contentDescription = ""
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Gray,
                    disabledTextColor = Color.Transparent,
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                maxLines = 1,
                onValueChange = { onValueChange(it) }
            )
        }
    }
}

@Composable
private fun DatePickerDialog(
    viewModel: MedicationDetailViewModel
) {
    val showDatePickerDialog by viewModel.showDatePickerDialog
    
    val dialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dialogState,
        onCloseRequest = {},
        buttons = {
            positiveButton(stringResource(id = R.string.medication_details_dialog_positive_button))
            negativeButton(
                stringResource(id = R.string.medication_details_dialog_negative_button),
                onClick = { viewModel.onDateChange(date = null) }
            )
        }
    ) {
        datepicker(
            title = stringResource(id = R.string.medication_details_dialog_title),
            onDateChange = { date -> viewModel.onDateChange(date = date) }
        )
    }

    if (showDatePickerDialog) {
        LaunchedEffect(
            key1 = "show_date_picker_dialog",
            block = {
                dialogState.show()
            }
        )
    } else {
        LaunchedEffect(
            key1 = "hide_date_picker_dialog",
            block = {
                dialogState.hide()
            }
        )
    }
}

@Composable
private fun TimePickerDialog(
    viewModel: MedicationDetailViewModel
) {
    val showTimePickerDialog by viewModel.showTimePickerDialog

    val dialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dialogState,
        onCloseRequest = {},
        buttons = {
            positiveButton(stringResource(id = R.string.medication_details_dialog_positive_button))
            negativeButton(
                stringResource(id = R.string.medication_details_dialog_negative_button),
                onClick = { viewModel.onTimeChange(time = null) }
            )
        }
    ) {
        timepicker(
            title = stringResource(id = R.string.medication_details_dialog_title),
            onTimeChange = { time -> viewModel.onTimeChange(time = time) }
        )
    }

    if (showTimePickerDialog) {
        LaunchedEffect(
            key1 = "show_time_picker_dialog",
            block = {
                dialogState.show()
            }
        )
    } else {
        LaunchedEffect(
            key1 = "hide_time_picker_dialog",
            block = {
                dialogState.hide()
            }
        )
    }
}

private fun String.increment(): String {
    return if (this.isNotBlank()) {
        this.toInt().inc().toString()
    } else {
        this
    }
}

private fun String.decrement(): String {
    return if (this.isNotBlank()) {
        val newValue = this.toInt().dec()
        if (newValue > 0) {
            newValue.toString()
        } else {
            "1"
        }
    } else {
        this
    }
}

@Composable
@Preview(showBackground = true)
fun MedicationDetailScreenPreview() {
    //MedicationDetailScreen()
    RepeatingIntervalRadioGroup(
        items = listOf(
            RepeatingIntervalVO.FOUR,
            RepeatingIntervalVO.SIX,
            RepeatingIntervalVO.EIGHT,
            RepeatingIntervalVO.TWELVE,
            RepeatingIntervalVO.CUSTOM,
        ),
        selectedItem = RepeatingIntervalVO.FOUR,
        onSelectionChanged = {  }
    )
}