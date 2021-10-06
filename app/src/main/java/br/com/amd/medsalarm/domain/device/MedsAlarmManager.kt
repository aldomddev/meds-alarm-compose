package br.com.amd.medsalarm.domain.device

import br.com.amd.medsalarm.domain.model.MedsAlarm
import java.time.LocalDateTime

interface MedsAlarmManager {
    fun set(alarm: MedsAlarm)
    fun cancel(alarm: MedsAlarm)
    fun getNextAlarm(alarm: MedsAlarm) : LocalDateTime?
}