package br.com.amd.medsalarm.presentation.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.amd.medsalarm.R
import br.com.amd.medsalarm.presentation.viewmodels.MedicationDetailViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker

@Composable
fun MedicationDetailScreen(
    viewModel: MedicationDetailViewModel = hiltViewModel(),
    medsAlarmId: Int
) {
    val medication by viewModel.medicationText.observeAsState("")
    val description by viewModel.descriptionText.observeAsState("")

    val startsOn by viewModel.startsOnDateTime.observeAsState("")
    val endsOn by viewModel.endsOnDateTime.observeAsState("")

    val showStartsOnDateTimeDialog by viewModel.showStartsOnDateTime.observeAsState(false)
    val showsEndsOnDateTimeDialog by viewModel.showEndsOnDateTime.observeAsState(false)

    val focusRequester = remember { FocusRequester() }
    val permanent by viewModel.endsOnDateTimeEnabled.observeAsState(false)

    LaunchedEffect(
        key1 = "edit_alarm",
        block = { viewModel.loadAlarmDataForEdition(medsAlarmId) }
    )

    val timePicker = remember { MaterialDialog() }
    timePicker.build(
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
            onTimeChange = { time -> viewModel.onTimeChange(time) }
        )
    }

    val datePicker = remember { MaterialDialog() }
    datePicker.build(
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
            onDateChange = { date ->
                viewModel.onDateChange(date)
                timePicker.show()
            }
        )
    }

    if (showStartsOnDateTimeDialog || showsEndsOnDateTimeDialog) {
        datePicker.show()
        viewModel.onDateTimeDialogShow()
    }

    var repeatingSelection by remember { mutableStateOf("6") }
    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .padding(top = 16.dp, bottom = 80.dp, start = 16.dp, end = 16.dp)
        .fillMaxWidth()
        .verticalScroll(scrollState)) {

        // medication
        TextField(
            value = medication,
            onValueChange = { viewModel.onMedicationTextChange(text = it) },
            singleLine = true,
            label = { Text(stringResource(id = R.string.medication_details_medication)) },
            placeholder = { Text(text = stringResource(id = R.string.medication_details_medication_placeholder)) },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(10)),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Gray,
                disabledTextColor = Color.Transparent,
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        // description
        TextField(
            value = description,
            onValueChange = { viewModel.onDescriptionTextChange(text = it) },
            singleLine = true,
            label = { Text(stringResource(id = R.string.medication_details_description)) },
            placeholder = { Text(text = stringResource(id = R.string.medication_details_description_placeholder)) },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(10)),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Gray,
                disabledTextColor = Color.Transparent,
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        // starts on
        TextField(
            value = startsOn,
            onValueChange = { },
            readOnly = true,
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.medication_details_start_on)) },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(10))
                .focusRequester(focusRequester)
                .onFocusChanged { viewModel.onStartsOnFocusChange(focused = it.hasFocus) }
                .focusTarget()
                .pointerInput(Unit) { detectTapGestures { focusRequester.requestFocus() } },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Gray,
                disabledTextColor = Color.Transparent,
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = permanent,
                onCheckedChange = { }
            )

            Spacer(modifier = Modifier.padding(end = 16.dp))

            Text(text = "Permanente")
        }

        // ends on
        if (!permanent) {
            TextField(
                value = endsOn,
                onValueChange = { },
                readOnly = true,
                singleLine = true,
                label = { Text(text = stringResource(id = R.string.medication_details_ends_on)) },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray, RoundedCornerShape(10))
                    .focusRequester(focusRequester)
                    .onFocusChanged { viewModel.onEndsOnFocusChange(focused = it.hasFocus) }
                    .focusTarget()
                    .pointerInput(Unit) { detectTapGestures { focusRequester.requestFocus() } },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Gray,
                    disabledTextColor = Color.Transparent,
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }

        RepeatingIntervalRadioGroup(
            items = listOf("4", "6", "8", "12", "Outro"),
            selectedItem = repeatingSelection,
            onSelectionChanged = { repeatingSelection = it }
        )

        if (repeatingSelection == "Outro") {
            CustomRepeatingInterval(isVisible = true)
        }

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
            onClick = { }
        ) {
            Text("Cancelar")
        }
    }
}

@Composable
private fun RepeatingIntervalRadioGroup(
    items: List<String>,
    selectedItem: String,
    onSelectionChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
        ) {

        Text(text = "Repetir a cada (horas)")

        Spacer(modifier = Modifier.padding(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEach { item ->
                RadioButton(
                    selected = item == selectedItem,
                    onClick = { onSelectionChanged.invoke(item) }
                )

                Spacer(modifier = Modifier.padding(2.dp))

                Text(text = item)

                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
private fun CustomRepeatingInterval(
    isVisible: Boolean
) {
    if (isVisible) {
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            //Button(onClick = { }) {
//            Image(
//                modifier = Modifier.padding(16.dp),
//                painter = painterResource(id = R.drawable.ic_plus),
//                colorFilter = ColorFilter.tint(Color.Black),
//                contentDescription = ""
//            )
            //}

            TextField(
                value = "24",
                modifier = Modifier.width(130.dp),
                trailingIcon = {
                    Image(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { },
                        painter = painterResource(id = R.drawable.ic_plus),
                        colorFilter = ColorFilter.tint(Color.Black),
                        contentDescription = ""
                    )
                },
                leadingIcon = {
                    Image(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { },
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
                onValueChange = { }
            )

            //Button(onClick = { }) {
//                Image(
//                    modifier = Modifier.padding(16.dp),
//                    painter = painterResource(id = R.drawable.ic_minus),
//                    colorFilter = ColorFilter.tint(Color.Black),
//                    contentDescription = ""
//                )
            //}
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MedicationDetailScreenPreview() {
    MedicationDetailScreen(medsAlarmId = 0)
}