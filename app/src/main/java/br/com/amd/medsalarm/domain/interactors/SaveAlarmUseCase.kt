package br.com.amd.medsalarm.domain.interactors

import br.com.amd.medsalarm.domain.model.MedsAlarm
import br.com.amd.medsalarm.domain.repository.MedsAlarmRepository
import javax.inject.Inject

class SaveAlarmUseCase @Inject constructor(
    private val alarmsRepository: MedsAlarmRepository
) {
    suspend operator fun invoke(alarm: MedsAlarm): Result<Unit> {
        return kotlin.runCatching {
            alarmsRepository.saveOrUpdate(alarm)
        }
    }
}