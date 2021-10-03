package br.com.amd.medsalarm.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import br.com.amd.medsalarm.device.model.MedsAlarmNotification
import br.com.amd.medsalarm.device.util.DeviceConstants
import br.com.amd.medsalarm.device.util.MedsAlarmNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MedsAlarmReceiver : BroadcastReceiver() {

    @Inject lateinit var notificationManager: MedsAlarmNotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Fired!", Toast.LENGTH_LONG).show()

        try {
            if (intent?.action == DeviceConstants.MEDS_ALARM_ACTION) {
                val alarm = intent.getParcelableExtra<MedsAlarmNotification>(DeviceConstants.MEDS_ALARM_NOTIFICATION_EXTRA)
                alarm?.let {
                    notificationManager.createMedsAlarmNotification(alarm)
                }

            }
        } catch (error: Throwable) {
            println(error)
        }
    }
}