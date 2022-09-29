package br.com.amd.medsalarm.presentation.model

data class MedsAlarmWithHistoryVO(
    val alarm: MedsAlarmVO,
    val history: List<MedsHistoryEntryVO>
)
