package br.com.amd.medsalarm.device

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import br.com.amd.medsalarm.device.mapper.toMedsAlarmNotification
import br.com.amd.medsalarm.device.util.DeviceConstants.MEDS_ALARM_ACTION
import br.com.amd.medsalarm.device.util.DeviceConstants.MEDS_ALARM_NOTIFICATION_EXTRA
import br.com.amd.medsalarm.domain.device.AlarmPermission
import br.com.amd.medsalarm.domain.device.MedsAlarmManager
import br.com.amd.medsalarm.domain.model.MedsAlarm
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

class MedsAlarmManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmPermission: AlarmPermission,
    private val alarmManager: AlarmManager?
) : MedsAlarmManager {

    override fun set(alarm: MedsAlarm): LocalDateTime? {
        if (alarmManager == null || !alarmPermission.hasExactAlarmPermission()) return null

        val next = getNextAlarm(alarm)
        next?.let { dateTime ->
            val pendingIntent = getPendingIntentFor(alarm = alarm)

            val zoneId = ZoneId.of(ZoneOffset.systemDefault().toString())
            val zoneOffset = zoneId.rules.getOffset(dateTime)

//            AlarmManagerCompat.setExactAndAllowWhileIdle(
//                alarmManager,
//                AlarmManager.RTC_WAKEUP,
//                dateTime.toInstant(zoneOffset).toEpochMilli(),
//                pendingIntent
//            )

            AlarmManagerCompat.setAlarmClock(
                alarmManager,
                dateTime.toInstant(zoneOffset).toEpochMilli(),
                pendingIntent,
                pendingIntent
            )

            println("AMD - Alarm set to $dateTime")
        }

        return next
    }

    override fun cancel(alarm: MedsAlarm) {
        if (alarmManager == null) return

        val pendingIntent = getPendingIntentFor(alarm = alarm)
        alarmManager.cancel(pendingIntent)
    }

    private fun getPendingIntentFor(alarm: MedsAlarm) : PendingIntent {
        val intent = Intent(context, MedsAlarmReceiver::class.java).apply {
            action = MEDS_ALARM_ACTION
            putExtra(MEDS_ALARM_NOTIFICATION_EXTRA, alarm.toMedsAlarmNotification())
        }

        return PendingIntent.getBroadcast(
            context,
            alarm.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    override fun getNextAlarm(alarm: MedsAlarm) : LocalDateTime? {
        val isAllowedToSchedule = alarm.enabled && alarm.endsOn == null || alarm.endsOn?.isAfter(LocalDateTime.now()) == true
        val nextAlarmIsValid = alarm.next?.isAfter(LocalDateTime.now()) == true

        return if (isAllowedToSchedule) {
            when {
                nextAlarmIsValid -> { alarm.next }
                else -> alarm.startsOn?.let { startsOnDateTime ->
                    if (startsOnDateTime.isAfter(LocalDateTime.now())) {
                        startsOnDateTime
                    } else {
                        alarm.next?.plusHours(alarm.repeatingInterval.toLong())
                        //alarm.next?.plusMinutes(alarm.repeatingInterval.toLong())
                    }
                }
            }
        } else {
            null
        }
    }
}