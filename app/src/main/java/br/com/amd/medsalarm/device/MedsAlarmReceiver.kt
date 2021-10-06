package br.com.amd.medsalarm.device

import android.app.AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.work.WorkManager
import br.com.amd.medsalarm.device.model.MedsAlarmNotification
import br.com.amd.medsalarm.device.util.DeviceConstants
import br.com.amd.medsalarm.device.util.MedsAlarmNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MedsAlarmReceiver : BroadcastReceiver() {

    @Inject lateinit var notificationManager: MedsAlarmNotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        Toast.makeText(context, "Fired!", Toast.LENGTH_LONG).show()

        try {
            when(intent?.action) {
                DeviceConstants.MEDS_ALARM_ACTION -> {
                    val alarm = intent.getParcelableExtra<MedsAlarmNotification>(DeviceConstants.MEDS_ALARM_NOTIFICATION_EXTRA)
                    alarm?.let {
                        notificationManager.createMedsAlarmNotification(alarm)
                        val workRequest = AlarmSchedulerWorker.schedule(alarmId = alarm.id)
                        WorkManager.getInstance(context).enqueue(workRequest)
                    }
                }
                ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> {
                    val workRequest = AlarmSchedulerWorker.schedule(alarmId = DeviceConstants.SCHEDULE_ALL_ALARMS)
                    WorkManager.getInstance(context).enqueue(workRequest)
                }
            }
        } catch (error: Throwable) {
            println(error)
        }
    }
}