package br.com.amd.medsalarm.device

import android.app.AlarmManager
import android.os.Build
import br.com.amd.medsalarm.domain.device.AlarmPermission
import javax.inject.Inject

class AlarmPermissionImpl @Inject constructor(
    private val alarmManager: AlarmManager?
): AlarmPermission {

    override fun hasExactAlarmPermission(): Boolean {
        if (alarmManager == null) return false

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }
}