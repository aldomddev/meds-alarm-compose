package br.com.amd.medsalarm.domain.device

import br.com.amd.medsalarm.domain.model.MedsAlarm

interface MedsAlarmManager {
    fun set(alarm: MedsAlarm)
    fun cancel(alarm: MedsAlarm)
}