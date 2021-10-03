package br.com.amd.medsalarm.device.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import br.com.amd.medsalarm.device.model.MedsAlarmNotification
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MedsAlarmNotificationManager @Inject constructor(
    @ApplicationContext private val appContext: Context
) {
    init {
        createNotificationChannel(appContext)
    }

    private fun createNotificationChannel(context: Context) {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelId = getChannelId()
            val channel = NotificationChannel(channelId, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_DESCRIPTION
            channel.setShowBadge(true)

            // Register the channel with the system
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createMedsAlarmNotification(alarm: MedsAlarmNotification) {
        val notification = NotificationCompat.Builder(appContext, getChannelId())
            .setContentTitle(alarm.medication)
            .setContentText(alarm.description)
            .setSmallIcon(alarm.icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .build()

        val notificationManager = NotificationManagerCompat.from(appContext)
        notificationManager.notify(0, notification)
    }

    private fun getChannelId(): String = "${appContext.packageName}-$CHANNEL_ID"

    internal companion object {
        const val CHANNEL_ID = "meds_alarm"
        const val CHANNEL_NAME = "Meds Alarm"
        const val CHANNEL_DESCRIPTION = "Meds Alarm"
    }
}