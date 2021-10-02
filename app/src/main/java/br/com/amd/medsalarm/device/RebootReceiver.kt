package br.com.amd.medsalarm.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.work.WorkManager
import javax.inject.Inject

class RebootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent?.action == Intent.ACTION_BOOT_COMPLETED) {

            val workRequest = AlarmRebootSchedulerWorker.schedule()
            WorkManager.getInstance(context).enqueue(workRequest)

            Toast.makeText(context, "Rebooted!", Toast.LENGTH_LONG).show()
        }
    }
}