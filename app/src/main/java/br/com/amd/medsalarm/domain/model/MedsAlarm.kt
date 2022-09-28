package br.com.amd.medsalarm.domain.model

import java.time.LocalDateTime

data class MedsAlarm(
    val id: Int = 0,
    val medication: String,
    val description: String = "",
    val startsOn: LocalDateTime? = null,
    val endsOn: LocalDateTime? = null,
    val next: LocalDateTime? = null,
    val repeatingInterval: Int = RepeatingInterval.EIGHT.interval,
    val repeatingIntervalUnit: RepeatingIntervalUnit = RepeatingIntervalUnit.HOUR,
    val enabled: Boolean = true
)
