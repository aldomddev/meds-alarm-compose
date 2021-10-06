package br.com.amd.medsalarm.data.mappers

import br.com.amd.medsalarm.data.model.MedsAlarmEntity
import br.com.amd.medsalarm.domain.model.MedsAlarm

fun MedsAlarmEntity.toDomain() = MedsAlarm(
        id = id,
        medication = medication,
        description = description,
        startsOn = startsOn,
        endsOn = endsOn,
        next = next,
        repeatingInterval = repeatingInterval,
        repeatingIntervalUnit = repeatingIntervalUnit,
        enabled = enabled,
        seen = seen
    )

fun List<MedsAlarmEntity>.toDomain() = map { it.toDomain() }

fun MedsAlarm.toEntity() = MedsAlarmEntity(
        id = id,
        medication = medication,
        description = description,
        startsOn = startsOn,
        endsOn = endsOn,
        next = next,
        repeatingInterval = repeatingInterval,
        repeatingIntervalUnit = repeatingIntervalUnit,
        enabled = enabled,
        seen = seen
    )

fun List<MedsAlarm>.toEntity() = map { it.toEntity() }