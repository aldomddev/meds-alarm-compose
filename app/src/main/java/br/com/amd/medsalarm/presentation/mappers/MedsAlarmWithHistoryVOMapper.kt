package br.com.amd.medsalarm.presentation.mappers

import br.com.amd.medsalarm.domain.model.MedsAlarmWithHistory
import br.com.amd.medsalarm.presentation.model.MedsAlarmVO
import br.com.amd.medsalarm.presentation.model.MedsAlarmWithHistoryVO

fun MedsAlarmWithHistory.toPresenter(): MedsAlarmWithHistoryVO {
    return MedsAlarmWithHistoryVO(
        alarm = MedsAlarmVO(
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
        history = history.toPresenter()
    )
}

fun List<MedsAlarmWithHistory>.toPresenter(): List<MedsAlarmWithHistoryVO> =
    map { it.toPresenter() }