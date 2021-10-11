package br.com.amd.medsalarm.presentation.model

data class TimeToNextAlarmVO(
    val isToday: Boolean,
    val isTomorrow: Boolean,
    val days: Long,
    val hours: Long,
    val minutes: Long
)
