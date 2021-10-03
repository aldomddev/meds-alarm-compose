package br.com.amd.medsalarm.device

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.AlarmManagerCompat
import br.com.amd.medsalarm.device.util.DeviceConstants.MEDS_ALARM_ACTION
import br.com.amd.medsalarm.device.util.DeviceConstants.MEDS_ALARM_NOTIFICATION_EXTRA
import br.com.amd.medsalarm.domain.device.MedsAlarmManager
import br.com.amd.medsalarm.domain.model.MedsAlarm
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

class MedsAlarmManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmManager: AlarmManager
) : MedsAlarmManager {

    override fun set(alarm: MedsAlarm) {
        alarm.next?.let { dateTime ->
            val pendingIntent = getPendingIntentFor(alarm = alarm)

            val zoneId = ZoneId.of(ZoneOffset.systemDefault().toString())
            val zoneOffset = zoneId.rules.getOffset(dateTime)

            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                AlarmManager.RTC_WAKEUP,
                dateTime.toInstant(zoneOffset).toEpochMilli() + 15000,
                pendingIntent
            )
        }
    }

    override fun cancel(alarm: MedsAlarm) {
        val pendingIntent = getPendingIntentFor(alarm = alarm)
        alarmManager.cancel(pendingIntent)
    }

    private fun getPendingIntentFor(alarm: MedsAlarm) : PendingIntent {
        val intent = Intent(context, MedsAlarmReceiver::class.java)
        intent.action = MEDS_ALARM_ACTION
        intent.putExtra(MEDS_ALARM_NOTIFICATION_EXTRA, alarm.toMedsAlarmNotification())

        return PendingIntent.getBroadcast(
            context,
            alarm.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )
    }
}