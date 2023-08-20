package br.com.amd.medsalarm.device

import android.app.AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.WorkManager
import br.com.amd.medsalarm.device.extensions.isScreenOn
import br.com.amd.medsalarm.device.model.MedsAlarmNotification
import br.com.amd.medsalarm.device.util.DeviceConstants
import br.com.amd.medsalarm.device.util.MedsAlarmNotificationManager
import br.com.amd.medsalarm.domain.device.MedsAlarmManager
import br.com.amd.medsalarm.domain.interactors.GetAlarmByIdUseCase
import br.com.amd.medsalarm.domain.interactors.GetEnabledAlarmsUseCase
import br.com.amd.medsalarm.domain.interactors.SaveAlarmUseCase
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MedsAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: MedsAlarmNotificationManager
    @Inject
    lateinit var getEnabledAlarmsUseCase: GetEnabledAlarmsUseCase
    @Inject
    lateinit var getAlarmByIdUseCase: GetAlarmByIdUseCase
    @Inject
    lateinit var saveAlarmUseCase: SaveAlarmUseCase
    @Inject
    lateinit var medsAlarmManager: MedsAlarmManager

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.i("Alarm fired! Action = ${intent?.action}, screen on = ${context.isScreenOn()}")

        if (context == null || intent == null) return

        try {
            when (intent.action) {
                DeviceConstants.MEDS_ALARM_ACTION -> {
                    val alarm = getAlarmParcelableDataFromIntent(intent)
                    alarm?.let {
                        notificationManager.createMedsAlarmNotification(alarm)
                        val workRequest = AlarmSchedulerWorker.schedule(alarmId = alarm.id)
                        WorkManager.getInstance(context).enqueue(workRequest)
                    }
                }

                DeviceConstants.MEDS_TAKEN_ACTION -> {
                    val alarmId = intent.getIntExtra(DeviceConstants.MEDS_TAKEN_ALARM_ID_EXTRA, 0)
                    notificationManager.cancelNotification(alarmId)
                }

                ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> {
                    val workRequest = AlarmSchedulerWorker.schedule(
                        alarmId = DeviceConstants.SCHEDULE_ALL_ALARMS
                    )
                    WorkManager.getInstance(context).enqueue(workRequest)
                }
            }
        } catch (error: Throwable) {
            Timber.e(error)
        }
    }

    private fun getAlarmParcelableDataFromIntent(intent: Intent): MedsAlarmNotification? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                DeviceConstants.MEDS_ALARM_NOTIFICATION_EXTRA,
                MedsAlarmNotification::class.java
            )
        } else {
            intent.getParcelableExtra(DeviceConstants.MEDS_ALARM_NOTIFICATION_EXTRA)
        }
    }
}