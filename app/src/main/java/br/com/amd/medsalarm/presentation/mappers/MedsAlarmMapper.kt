package br.com.amd.medsalarm.presentation.mappers

import br.com.amd.medsalarm.domain.model.MedsAlarm
import br.com.amd.medsalarm.presentation.model.MedsAlarmVO

fun MedsAlarm.toPresenter() = MedsAlarmVO(
        id = id,
        medication = medication,
        description = description,
        startsOn = startsOn,
        endsOn = endsOn,
        repeatingInterval = repeatingInterval,
        repeatingIntervalUnit = repeatingIntervalUnit,
        seen = seen
    )

fun List<MedsAlarm>.toPresenter() = map { it.toPresenter() }

fun MedsAlarmVO.toDomain() = MedsAlarm(
        id = id,
        medication = medication,
        description = description,
        startsOn = startsOn,
        endsOn = endsOn,
        repeatingInterval = repeatingInterval,
        repeatingIntervalUnit = repeatingIntervalUnit,
        seen = seen
)

fun List<MedsAlarmVO>.toDomain() = map { it.toDomain() }