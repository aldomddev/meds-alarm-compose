package br.com.amd.medsalarm.presentation.model

sealed class MedsAlarmListState {
    object Empty : MedsAlarmListState()
    object Loading : MedsAlarmListState()
    data class Loaded(val data: List<MedsAlarmVO>) : MedsAlarmListState()
    object Error : MedsAlarmListState()
}
