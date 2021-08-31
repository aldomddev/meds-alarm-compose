package br.com.amd.medsalarm.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.amd.medsalarm.R
import br.com.amd.medsalarm.ui.theme.MedsAlarmTheme
import br.com.amd.medsalarm.ui.widgets.DateTimePickerDialog

@Composable
fun MedicationDetailScreen() {
    var medication by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var startsOn by remember { mutableStateOf(TextFieldValue("")) }
    var endsOn by remember { mutableStateOf(TextFieldValue("")) }

    val focusRequester = FocusRequester()
    var showStartsOnDateTimeDialog by remember { mutableStateOf(false) }
    var permanent by remember { mutableStateOf(false) }
    var showsEndsOnDateTimeDialog by remember { mutableStateOf(false) }
    DateTimePickerDialog(
        showDialog = (showStartsOnDateTimeDialog || showsEndsOnDateTimeDialog),
        onDialogClosed = {
            showStartsOnDateTimeDialog = false
            showsEndsOnDateTimeDialog = false
            focusRequester.freeFocus()
        }
    )

    var repeatingSelection by remember { mutableStateOf("6") }
    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .padding(top = 16.dp, bottom = 80.dp, start = 16.dp, end = 16.dp)
        .fillMaxWidth()
        .verticalScroll(scrollState)) {

        TextField(
            value = medication,
            onValueChange = { medication = it },
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

        TextField(
            value = description,
            onValueChange = { description = it },
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

        TextField(
            value = startsOn,
            onValueChange = { startsOn = it },
            readOnly = true,
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.medication_details_start_on)) },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(10))
                .focusRequester(focusRequester)
                .onFocusChanged { showStartsOnDateTimeDialog = it.hasFocus }
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
                onCheckedChange = { permanent = it }
            )

            Spacer(modifier = Modifier.padding(end = 16.dp))

            Text(text = "Permanente")
        }

        TextField(
            enabled = permanent,
            value = endsOn,
            onValueChange = { endsOn = it },
            readOnly = true,
            singleLine = true,
            label = { Text(text = stringResource(id = R.string.medication_details_ends_on)) },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(10))
                .focusRequester(focusRequester)
                .onFocusChanged { showsEndsOnDateTimeDialog = it.hasFocus }
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

        RepeatingIntervalRadioGroup(
            items = listOf("4", "6", "8", "12", "Outro"),
            selectedItem = repeatingSelection,
            onSelectionChanged = { repeatingSelection = it }
        )

        CustomRepeatingInterval(isVisible = true)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { }) {
                Text("Salvar")
            }

            Button(onClick = { }) {
                Text("Cancelar")
            }
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
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
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
                        modifier = Modifier.padding(8.dp).clickable {  },
                        painter = painterResource(id = R.drawable.ic_plus),
                        colorFilter = ColorFilter.tint(Color.Black),
                        contentDescription = ""
                    )
                },
                leadingIcon = {
                    Image(
                        modifier = Modifier.padding(8.dp).clickable {  },
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
    MedicationDetailScreen()
}