package br.com.amd.medsalarm.domain.model

data class MedsAlarmWithHistory(
    val alarm: MedsAlarm,
    val history: List<MedsHistoryEntry>
)
