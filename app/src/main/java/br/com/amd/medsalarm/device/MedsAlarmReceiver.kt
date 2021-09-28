package br.com.amd.medsalarm.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import br.com.amd.medsalarm.device.util.DeviceConstants

class MedsAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == DeviceConstants.MEDS_ALARM_ACTION) {
            Toast.makeText(context, "Fired!", Toast.LENGTH_LONG).show()
        }
    }
}