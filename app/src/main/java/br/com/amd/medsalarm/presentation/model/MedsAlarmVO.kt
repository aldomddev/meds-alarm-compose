package br.com.amd.medsalarm.presentation.model

import br.com.amd.medsalarm.domain.model.RepeatingInterval
import br.com.amd.medsalarm.domain.model.RepeatingIntervalUnit
import java.time.LocalDateTime

data class MedsAlarmVO(
    val id: Int = 0,
    val medication: String,
    val description: String = "",
    val startsOn: LocalDateTime,
    val endsOn: LocalDateTime? = null,
    val repeatingInterval: RepeatingInterval = RepeatingInterval.EIGHT,
    val repeatingIntervalUnit: RepeatingIntervalUnit = RepeatingIntervalUnit.HOUR,
    val seen: Boolean = false
)
