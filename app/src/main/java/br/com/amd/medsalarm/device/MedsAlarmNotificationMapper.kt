package br.com.amd.medsalarm.device

import br.com.amd.medsalarm.device.model.MedsAlarmNotification
import br.com.amd.medsalarm.domain.model.MedsAlarm

fun MedsAlarm.toMedsAlarmNotification(): MedsAlarmNotification {
    return MedsAlarmNotification(
        id = id,
        medication = medication,
        description = description
    )
}