package br.com.amd.medsalarm.device

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import br.com.amd.medsalarm.domain.device.MedsAlarmManager
import br.com.amd.medsalarm.domain.interactors.GetEnabledAlarmsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AlarmRebootSchedulerWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val getEnabledAlarmsUseCase: GetEnabledAlarmsUseCase,
    private val medsAlarmManager: MedsAlarmManager
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        val result = getEnabledAlarmsUseCase().fold(
            onSuccess = { alarms ->
                alarms.forEach { alarm ->
                    medsAlarmManager.set(alarm)
                }

                Result.success()
            },
            onFailure = { Result.failure() }
        )

        return result
    }

    companion object {

        fun schedule(): WorkRequest {
            return OneTimeWorkRequestBuilder<AlarmRebootSchedulerWorker>()
                    .build()
        }
    }
}