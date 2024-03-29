package br.com.amd.medsalarm.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import br.com.amd.medsalarm.R
import br.com.amd.medsalarm.presentation.extensions.toFormattedString
import br.com.amd.medsalarm.presentation.model.MedsAlarmActionVO
import br.com.amd.medsalarm.presentation.model.MedsAlarmListState
import br.com.amd.medsalarm.presentation.model.MedsAlarmVO
import br.com.amd.medsalarm.presentation.model.TimeToNextAlarmVO
import java.time.LocalDateTime

@ExperimentalMaterialApi
@Composable
fun MedsListScreen(
    viewState: MedsAlarmListState,
    onEditItemRequest: (MedsAlarmVO) -> Unit,
    onDeleteItemRequest: (MedsAlarmVO) -> Unit
) {
    when(viewState) {
        MedsAlarmListState.Empty -> EmptyList()
        MedsAlarmListState.Error -> {}
        is MedsAlarmListState.Loaded -> {
            MedsAlarmList(
                alarms = viewState.data,
                onEditItemRequest = onEditItemRequest,
                onDeleteItemRequest = onDeleteItemRequest
            )
        }
        MedsAlarmListState.Loading -> {}
    }
}

@ExperimentalMaterialApi
@Composable
private fun MedsAlarmList(
    alarms: List<MedsAlarmVO>,
    onEditItemRequest: (MedsAlarmVO) -> Unit,
    onDeleteItemRequest: (MedsAlarmVO) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(all = 8.dp),
        content = {
            items(alarms) { alarm ->
                MedsAlarmItem(
                    alarmVO = alarm,
                    onEditItemRequest = onEditItemRequest,
                    onDeleteItemRequest = onDeleteItemRequest
                )
            }
        }
    )
}

@ExperimentalMaterialApi // added because of Card
@Composable
private fun MedsAlarmItem(
    alarmVO: MedsAlarmVO,
    onEditItemRequest: (MedsAlarmVO) -> Unit,
    onDeleteItemRequest: (MedsAlarmVO) -> Unit
) {
    val showMenuItem = remember { mutableStateOf(false) }
    val position = remember { mutableStateOf(Offset(x = 0f, y = 0f)) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 5.dp,
        onClick = { onEditItemRequest(alarmVO.copy(action = MedsAlarmActionVO.EDIT)) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = alarmVO.next?.toLocalTime().toFormattedString())

            Spacer(modifier = Modifier.padding(start = 16.dp))

            Column {
                Text(alarmVO.medication)
                Spacer(modifier = Modifier.padding(start = 8.dp))
                Text(alarmVO.description)

                val nextAlarmString = alarmVO.toNextAlarmString()
                if (nextAlarmString.isNotEmpty()) {
                    Spacer(modifier = Modifier.padding(start = 8.dp))
                    Text(
                        color = Color.Gray,
                        text = alarmVO.toNextAlarmString()
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1.0f))

            Image(
                modifier = Modifier
                    .padding(8.dp)
                    .onGloballyPositioned { coordinates ->
                        position.value = coordinates.positionInParent()
                    }
                    .clickable { showMenuItem.value = true },
                painter = painterResource(id = R.drawable.ic_more_vertical),
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                contentDescription = ""
            )
        }

        ItemDropdownMenu(
            expanded = showMenuItem.value,
            offset = DpOffset(x = position.value.x.dp, y = -position.value.y.dp),
            onEditClick = {
                onEditItemRequest(alarmVO.copy(action = MedsAlarmActionVO.EDIT))
                showMenuItem.value = false
            },
            onDeleteClick = {
                onDeleteItemRequest(alarmVO.copy(action = MedsAlarmActionVO.DELETE))
                showMenuItem.value = false
            },
            onDismissRequest = { showMenuItem.value = false }
        )
    }
}

@Composable
private fun ItemDropdownMenu(
    expanded: Boolean,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    DropdownMenu(
        offset = offset,
        expanded = expanded,
        onDismissRequest = { onDismissRequest() }
    ) {
        DropdownMenuItem(
            onClick = { onEditClick() }
        ) {
            Text(stringResource(id = R.string.common_edit))
        }
        DropdownMenuItem(
            onClick = { onDeleteClick() }
        ) {
            Text(stringResource(id = R.string.common_delete))
        }
    }
}

@Composable
private fun EmptyList() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.alarm_list_no_medications)
        )
    }
}

private fun MedsAlarmVO.toNextAlarmString(): String {
    if (this.next == null || this.timeToNextAlarmVO == null) {
        return ""
    }
    return when {
        this.timeToNextAlarmVO.isTomorrow -> "Amanhã às ${this.next.toLocalTime().toFormattedString()}"
        this.timeToNextAlarmVO.days > 0 -> {
            var str = "Em ${this.timeToNextAlarmVO.days} dia(s)"
            if (this.timeToNextAlarmVO.hours > 0) {
                str += ", ${this.timeToNextAlarmVO.hours} hora(s)"
            }

            str
        }
        this.timeToNextAlarmVO.hours > 0 -> {
            var str = "Em ${this.timeToNextAlarmVO.hours} hora(s)"
            if (this.timeToNextAlarmVO.minutes > 0) {
                str += ", ${this.timeToNextAlarmVO.minutes} minuto(s)"
            }

            str
        }
        this.timeToNextAlarmVO.minutes > 0 -> {
            "Em ${this.timeToNextAlarmVO.minutes} minuto(s)"
        }
        else -> ""
    }
}

@ExperimentalMaterialApi
@Composable
@Preview(showBackground = true)
private fun MedsAlarmItemPreview() {
    MedsAlarmItem(
        alarmVO = MedsAlarmVO(
            id = 1,
            medication = "Tilenol",
            description = "Tomar 30 gotas",
            startsOn = LocalDateTime.now()
        ),
        onEditItemRequest = {},
        onDeleteItemRequest = {}
    )
}