package br.com.amd.medsalarm.device

import android.app.AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import br.com.amd.medsalarm.device.model.MedsAlarmNotification
import br.com.amd.medsalarm.device.util.DeviceConstants
import br.com.amd.medsalarm.device.util.MedsAlarmNotificationManager
import br.com.amd.medsalarm.domain.device.MedsAlarmManager
import br.com.amd.medsalarm.domain.interactors.GetAlarmByIdUseCase
import br.com.amd.medsalarm.domain.interactors.GetEnabledAlarmsUseCase
import br.com.amd.medsalarm.domain.interactors.SaveAlarmUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MedsAlarmReceiver : BroadcastReceiver() {

    @Inject lateinit var notificationManager: MedsAlarmNotificationManager

    @Inject lateinit var getEnabledAlarmsUseCase: GetEnabledAlarmsUseCase
    @Inject lateinit var getAlarmByIdUseCase: GetAlarmByIdUseCase
    @Inject lateinit var saveAlarmUseCase: SaveAlarmUseCase
    @Inject lateinit var medsAlarmManager: MedsAlarmManager
    val coroutineScope = CoroutineScope(SupervisorJob())

    override fun onReceive(context: Context?, intent: Intent?) {
        println("AMD - Alarm fired! Action = ${intent?.action}")

        if (context == null) return

        coroutineScope.launch {
            handleAction(intent)
        }

//        try {
//            when(intent?.action) {
//                DeviceConstants.MEDS_ALARM_ACTION -> {
//                    val alarm = intent.getParcelableExtra<MedsAlarmNotification>(DeviceConstants.MEDS_ALARM_NOTIFICATION_EXTRA)
//                    alarm?.let {
//                        notificationManager.createMedsAlarmNotification(alarm)
//                        val workRequest = AlarmSchedulerWorker.schedule(alarmId = alarm.id)
//                        WorkManager.getInstance(context).enqueue(workRequest)
//                    }
//                }
//                ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> {
//                    val workRequest = AlarmSchedulerWorker.schedule(alarmId = DeviceConstants.SCHEDULE_ALL_ALARMS)
//                    WorkManager.getInstance(context).enqueue(workRequest)
//                }
//            }
//        } catch (error: Throwable) {
//            println(error)
//        }
    }

    private suspend fun handleAction(intent: Intent?) {
        when(intent?.action) {
            DeviceConstants.MEDS_ALARM_ACTION -> {
                val alarm = intent.getParcelableExtra<MedsAlarmNotification>(DeviceConstants.MEDS_ALARM_NOTIFICATION_EXTRA)
                alarm?.let {
                    notificationManager.createMedsAlarmNotification(alarm)

                    if (alarm.id == DeviceConstants.SCHEDULE_ALL_ALARMS) {
                        scheduleAllAlarms()
                    } else if (alarm.id > 0) {
                        scheduleAlarm(alarm.id)
                    }
                }
            }
            ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> {
                scheduleAllAlarms()
            }
        }
    }

    private suspend fun scheduleAlarm(alarmId: Int) {
        getAlarmByIdUseCase(alarmId).fold(
            onSuccess = { alarm ->
                val next = medsAlarmManager.set(alarm)
                saveAlarmUseCase(alarm.copy(next = next))
            },
            onFailure = { }
        )
    }

    private suspend fun scheduleAllAlarms() {
        getEnabledAlarmsUseCase().fold(
            onSuccess = { alarms ->
                alarms.forEach { alarm ->
                    medsAlarmManager.set(alarm)
                }
            },
            onFailure = { }
        )
    }
}