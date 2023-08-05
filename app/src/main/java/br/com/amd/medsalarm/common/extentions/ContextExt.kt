package br.com.amd.medsalarm.common.extentions

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context

fun Context.getAlarmManager() =
    getSystemService(Context.ALARM_SERVICE) as? AlarmManager

fun Context.getNotificationManager() =
    getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager