package br.com.amd.medsalarm.data.mappers

import br.com.amd.medsalarm.data.model.MedsAlarmWithHistoryEntity
import br.com.amd.medsalarm.domain.model.MedsAlarm
import br.com.amd.medsalarm.domain.model.MedsAlarmWithHistory

fun MedsAlarmWithHistoryEntity.toDomain(): MedsAlarmWithHistory {
    return MedsAlarmWithHistory(
        alarm = MedsAlarm(
            id = alarm.id,
            medication = alarm.medication,
            description = alarm.description,
            startsOn = alarm.startsOn,
            endsOn = alarm.endsOn,
            next = alarm.next,
            repeatingInterval = alarm.repeatingInterval,
            repeatingIntervalUnit = alarm.repeatingIntervalUnit,
            enabled = alarm.enabled
        ),
        history = history.toDomain()
    )
}

fun List<MedsAlarmWithHistoryEntity>.toDomain(): List<MedsAlarmWithHistory> = map { it.toDomain() }