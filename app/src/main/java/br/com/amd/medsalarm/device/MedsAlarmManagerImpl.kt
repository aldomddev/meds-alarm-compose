package br.com.amd.medsalarm.device

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import br.com.amd.medsalarm.device.mapper.toMedsAlarmNotification
import br.com.amd.medsalarm.device.util.DeviceConstants.MEDS_ALARM_ACTION
import br.com.amd.medsalarm.device.util.DeviceConstants.MEDS_ALARM_NOTIFICATION_EXTRA
import br.com.amd.medsalarm.domain.device.PermissionChecker
import br.com.amd.medsalarm.domain.device.MedsAlarmManager
import br.com.amd.medsalarm.domain.model.MedsAlarm
import br.com.amd.medsalarm.domain.model.RepeatingIntervalUnit
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

class MedsAlarmManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val permissionChecker: PermissionChecker,
    private val alarmManager: AlarmManager?
) : MedsAlarmManager {

    override fun set(alarm: MedsAlarm): LocalDateTime? {
        if (alarmManager == null) {
            Timber.d("Cannot set alarm: alarmManager is null.")
            return null
        }

        if (!permissionChecker.hasExactAlarmPermission()) {
            Timber.d("Cannot set alarm: hasExactAlarmPermission was revoked.")
            return null
        }

        Timber.d("Setting alarm for id ${alarm.id}")

        val next = getNextAlarm(alarm)
        next?.let { dateTime ->
            val pendingIntent = getPendingIntentFor(alarm = alarm)

            val zoneId = ZoneId.of(ZoneOffset.systemDefault().toString())
            val zoneOffset = zoneId.rules.getOffset(dateTime)

            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                AlarmManager.RTC_WAKEUP,
                dateTime.toInstant(zoneOffset).toEpochMilli(),
                pendingIntent
            )

            Timber.d("Alarm ID ${alarm.id} set to $dateTime")
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
        val timeNow = LocalDateTime.now()
        val isAllowedToSchedule = alarm.enabled && (alarm.endsOn == null || alarm.endsOn.isAfter(timeNow))
        val nextAlarmIsValid = alarm.next?.isAfter(timeNow) == true // if false, we have a missed alarm

        Timber.d("isAllowedToSchedule=$isAllowedToSchedule, nextAlarmIsValid=$nextAlarmIsValid")

        return if (isAllowedToSchedule) {
            when {
                nextAlarmIsValid -> { alarm.next }
                else -> alarm.startsOn?.let { startsOnDateTime ->
                    if (startsOnDateTime.isAfter(timeNow)) {
                        startsOnDateTime
                    } else {
                        var next = alarm.next ?: startsOnDateTime

                        do {
                            next = updateAlarmTime(
                                nextAlarm = next,
                                repeatingValue = alarm.repeatingInterval,
                                repeatingIntervalUnit = alarm.repeatingIntervalUnit
                            )
                        } while (timeNow.isAfter(next))

                        next
                    }
                }
            }
        } else {
            null
        }
    }

    private fun updateAlarmTime(
        nextAlarm: LocalDateTime,
        repeatingValue: Int,
        repeatingIntervalUnit: RepeatingIntervalUnit
    ): LocalDateTime {
        return when (repeatingIntervalUnit) {
            RepeatingIntervalUnit.MINUTE -> nextAlarm.plusMinutes(repeatingValue.toLong())
            RepeatingIntervalUnit.HOUR -> nextAlarm.plusHours(repeatingValue.toLong())
            RepeatingIntervalUnit.DAY -> nextAlarm.plusDays(repeatingValue.toLong())
        }
    }
}