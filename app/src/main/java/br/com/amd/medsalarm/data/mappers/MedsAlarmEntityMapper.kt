package br.com.amd.medsalarm.data.mappers

import br.com.amd.medsalarm.data.model.MedsAlarmEntity
import br.com.amd.medsalarm.data.model.MedsAlarmWithHistoryEntity
import br.com.amd.medsalarm.data.model.MedsHistoryEntryEntity
import br.com.amd.medsalarm.domain.model.MedsAlarm
import br.com.amd.medsalarm.domain.model.MedsAlarmWithHistory
import br.com.amd.medsalarm.domain.model.MedsHistoryEntry

fun MedsAlarmEntity.toDomain() = MedsAlarm(
    id = id,
    medication = medication,
    description = description,
    startsOn = startsOn,
    endsOn = endsOn,
    next = next,
    repeatingInterval = repeatingInterval,
    repeatingIntervalUnit = repeatingIntervalUnit,
    enabled = enabled
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
    enabled = enabled
)

fun List<MedsAlarm>.toEntity() = map { it.toEntity() }