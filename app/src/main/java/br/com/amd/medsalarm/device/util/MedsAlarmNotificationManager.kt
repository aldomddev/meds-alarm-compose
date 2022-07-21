package br.com.amd.medsalarm.device.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import br.com.amd.medsalarm.R
import br.com.amd.medsalarm.core.extentions.getNotificationManager
import br.com.amd.medsalarm.device.MedsAlarmReceiver
import br.com.amd.medsalarm.device.model.MedsAlarmNotification
import br.com.amd.medsalarm.device.util.DeviceConstants.MEDS_TAKEN_ACTION
import br.com.amd.medsalarm.device.util.DeviceConstants.MEDS_TAKEN_ALARM_ID_EXTRA
import br.com.amd.medsalarm.presentation.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MedsAlarmNotificationManager @Inject constructor(
    @ApplicationContext private val appContext: Context
) {
    init {
        createNotificationChannel(appContext)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelId = getChannelId()
            val channel =
                NotificationChannel(channelId, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.description = CHANNEL_DESCRIPTION
            channel.setShowBadge(true)

            val notificationManager = context.getNotificationManager()
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun createMedsAlarmNotification(alarm: MedsAlarmNotification) {
        val notification = NotificationCompat.Builder(appContext, getChannelId())
            .setContentTitle(alarm.medication)
            .setContentText(alarm.description)
            .setSmallIcon(alarm.icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(getMedsAlarmIntent())
            .setAutoCancel(false)
            .addAction(getMedicineTakenAction(alarmId = alarm.id))
            .build()

        val notificationManager = NotificationManagerCompat.from(appContext)
        notificationManager.notify(TAG, alarm.id, notification)
    }

    fun cancelNotification(notificationId: Int) {
        val notificationManager = appContext.getNotificationManager()
        notificationManager?.cancel(TAG, notificationId)
    }

    @OptIn(ExperimentalMaterialApi::class)
    private fun getMedsAlarmIntent(): PendingIntent {
        val intent = Intent(appContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent.getActivity(
            appContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getMedicineTakenAction(alarmId: Int): NotificationCompat.Action {
        val medicineTakenIntent = Intent(appContext, MedsAlarmReceiver::class.java).apply {
            action = MEDS_TAKEN_ACTION
            putExtra(MEDS_TAKEN_ALARM_ID_EXTRA, alarmId)
        }

        val medicineTakenPendingIntent = PendingIntent.getBroadcast(
            appContext,
            0,
            medicineTakenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Action(
            R.drawable.ic_plus,
            appContext.getString(R.string.meds_taken_action_button),
            medicineTakenPendingIntent
        )
    }

    private fun getChannelId(): String = "${appContext.packageName}-$CHANNEL_ID"

    internal companion object {
        const val TAG = "MedsAlarmNotificationManager"
        const val CHANNEL_ID = "meds_alarm"
        const val CHANNEL_NAME = "Meds Alarm"
        const val CHANNEL_DESCRIPTION = "Meds Alarm"
    }
}