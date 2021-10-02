package br.com.amd.medsalarm.domain.interactors

import br.com.amd.medsalarm.domain.model.MedsAlarm
import br.com.amd.medsalarm.domain.repository.MedsAlarmRepository
import javax.inject.Inject

class GetEnabledAlarmsUseCase @Inject constructor(
    private val alarmsRepository: MedsAlarmRepository
) {
    suspend operator fun invoke(): Result<List<MedsAlarm>> {
        return kotlin.runCatching {
            alarmsRepository.getEnabled()
        }
    }
}