package br.com.amd.medsalarm.domain.interactors

import br.com.amd.medsalarm.domain.model.MedsAlarm
import br.com.amd.medsalarm.domain.repository.MedsAlarmRepository
import javax.inject.Inject

class SaveAlarmUseCase @Inject constructor(
    private val alarmsRepository: MedsAlarmRepository
) {
    suspend operator fun invoke(params: Params): Result<Unit> {
        return kotlin.runCatching {
            alarmsRepository.saveOrUpdate(alarm = params.alarm)
        }
    }

    data class Params(
        val alarm: MedsAlarm
    )
}