package br.com.amd.medsalarm.presentation.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import br.com.amd.medsalarm.presentation.model.MedsAlarmVO
import br.com.amd.medsalarm.presentation.viewmodels.TodayMedsViewModel
import java.time.LocalDateTime

@ExperimentalMaterialApi
@Composable
fun TodayMedsScreen(
    viewModel: TodayMedsViewModel
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val alarmsLifecycleAware = remember(viewModel.state, lifecycleOwner) {
        viewModel.state.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }.collectAsState(TodayMedsViewModel.ViewState.Empty)

    when(alarmsLifecycleAware.value) {
        TodayMedsViewModel.ViewState.Empty -> Text("Empty")
        TodayMedsViewModel.ViewState.Error -> TODO()
        is TodayMedsViewModel.ViewState.Loaded -> MedsAlarmList(alarms = (alarmsLifecycleAware.value as TodayMedsViewModel.ViewState.Loaded).data)
        TodayMedsViewModel.ViewState.Loading -> TODO()
    }
}

@ExperimentalMaterialApi
@Composable
private fun MedsAlarmList(alarms: List<MedsAlarmVO>) {
    LazyColumn(
        content = {
            items(alarms) { alarm ->
                MedsAlarmItem(alarmVO = alarm, onClick = {})
            }
        }
    )
}

@ExperimentalMaterialApi
@Composable
private fun MedsAlarmItem(
    alarmVO: MedsAlarmVO,
    onClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        onClick = { onClick(alarmVO.id) }
    ) {
        Text(alarmVO.medication)
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
        onClick = {}
    )
}