package br.com.amd.medsalarm.device

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import br.com.amd.medsalarm.device.util.DeviceConstants.SCHEDULE_ALL_ALARMS
import br.com.amd.medsalarm.domain.device.MedsAlarmManager
import br.com.amd.medsalarm.domain.interactors.GetAlarmByIdUseCase
import br.com.amd.medsalarm.domain.interactors.GetEnabledAlarmsUseCase
import br.com.amd.medsalarm.domain.interactors.SaveAlarmUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class AlarmSchedulerWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val getEnabledAlarmsUseCase: GetEnabledAlarmsUseCase,
    private val getAlarmByIdUseCase: GetAlarmByIdUseCase,
    private val saveAlarmUseCase: SaveAlarmUseCase,
    private val medsAlarmManager: MedsAlarmManager
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val alarmId = inputData.getInt(ALARM_ID, 0)

        when {
            alarmId == SCHEDULE_ALL_ALARMS -> {
                getEnabledAlarmsUseCase().fold(
                    onSuccess = { alarms ->
                        alarms.forEach { alarm ->
                            medsAlarmManager.set(alarm)
                        }

                        Result.success()
                    },
                    onFailure = { Result.failure() }
                )
            }
            alarmId > 0 -> {
                getAlarmByIdUseCase(alarmId).fold(
                    onSuccess = { alarm ->
                        val next = medsAlarmManager.set(alarm)
                        saveAlarmUseCase(alarm.copy(next = next))

                        Result.success()
                    },
                    onFailure = { Result.failure() }
                )
            }
            else -> {
                Result.failure()
            }
        }
    }

    companion object {
        private const val TAG = "Alarm Scheduler Worker"
        private const val ALARM_ID = "alarm.id"

        fun schedule(alarmId: Int): WorkRequest {
            val inputData = Data.Builder()
                .putInt(ALARM_ID, alarmId)
                .build()

            return OneTimeWorkRequest.Builder(AlarmSchedulerWorker::class.java)
                .addTag(TAG)
                .setInputData(inputData)
                .build()
        }
    }
}