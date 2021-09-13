package br.com.amd.medsalarm.domain.interactors

import br.com.amd.medsalarm.domain.model.MedsAlarm
import br.com.amd.medsalarm.domain.repository.MedsAlarmRepository
import javax.inject.Inject

class GetAlarmByIdUseCase @Inject constructor(
    private val alarmsRepository: MedsAlarmRepository
) {
    suspend operator fun invoke(params: Params): Result<MedsAlarm> {
        return kotlin.runCatching {
            alarmsRepository.getAlarmById(id = params.id)
        }
    }

    data class Params(
        val id: Int
    )
}