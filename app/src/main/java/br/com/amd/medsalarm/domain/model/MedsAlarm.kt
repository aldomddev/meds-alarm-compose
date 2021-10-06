package br.com.amd.medsalarm.domain.model

import java.time.LocalDateTime

data class MedsAlarm(
    val id: Int = 0,
    val medication: String,
    val description: String = "",
    val startsOn: LocalDateTime? = null,
    val endsOn: LocalDateTime? = null,
    val next: LocalDateTime? = null,
    val repeatingInterval: RepeatingInterval = RepeatingInterval.EIGHT,
    val repeatingIntervalUnit: RepeatingIntervalUnit = RepeatingIntervalUnit.HOUR,
    val enabled: Boolean = true,
    val seen: Boolean = false
)
