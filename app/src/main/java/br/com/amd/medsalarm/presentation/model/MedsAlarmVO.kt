package br.com.amd.medsalarm.presentation.model

import br.com.amd.medsalarm.domain.model.RepeatingInterval
import br.com.amd.medsalarm.domain.model.RepeatingIntervalUnit
import java.time.LocalDateTime

data class MedsAlarmVO(
    val id: Int = 0,
    val medication: String,
    val description: String = "",
    val startsOn: LocalDateTime? = null,
    val endsOn: LocalDateTime? = null,
    val next: LocalDateTime? = null,
    val timeToNextAlarmVO: TimeToNextAlarmVO? = null,
    val repeatingInterval: Int = RepeatingInterval.EIGHT.interval,
    val repeatingIntervalUnit: RepeatingIntervalUnit = RepeatingIntervalUnit.HOUR,
    val seen: Boolean = false,
    val enabled: Boolean = true,
    val action: MedsAlarmActionVO = MedsAlarmActionVO.EDIT
)
