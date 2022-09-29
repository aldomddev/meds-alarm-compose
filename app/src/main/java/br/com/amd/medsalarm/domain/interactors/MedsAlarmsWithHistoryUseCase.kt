package br.com.amd.medsalarm.domain.interactors

import br.com.amd.medsalarm.domain.model.MedsAlarmWithHistory
import br.com.amd.medsalarm.domain.repository.MedsAlarmRepository
import javax.inject.Inject

class MedsAlarmsWithHistoryUseCase @Inject constructor(
    private val alarmsRepository: MedsAlarmRepository
) {
    suspend operator fun invoke(): Result<List<MedsAlarmWithHistory>> {
        return kotlin.runCatching {
            alarmsRepository.getAlarmsWithHistory()
        }
    }
}