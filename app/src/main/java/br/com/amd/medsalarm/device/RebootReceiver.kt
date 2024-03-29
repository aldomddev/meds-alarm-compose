package br.com.amd.medsalarm.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.WorkManager
import br.com.amd.medsalarm.device.util.DeviceConstants.SCHEDULE_ALL_ALARMS
import timber.log.Timber

class RebootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("RebootReceiver called!")

        if (context != null && intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val workRequest = AlarmSchedulerWorker.schedule(alarmId = SCHEDULE_ALL_ALARMS)
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}