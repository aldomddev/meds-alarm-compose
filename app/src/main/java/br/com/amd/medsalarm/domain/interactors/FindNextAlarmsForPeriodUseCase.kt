package br.com.amd.medsalarm.domain.interactors

import br.com.amd.medsalarm.domain.model.MedsAlarm
import br.com.amd.medsalarm.domain.repository.MedsAlarmRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

class FindNextAlarmsForPeriodUseCase @Inject constructor(
    private val alarmsRepository: MedsAlarmRepository
) {
    suspend operator fun invoke(params: Params): Flow<List<MedsAlarm>> {
        return alarmsRepository.findNextAlarmsForPeriod(
            from = params.from,
            to = params.to,
            enabled = params.enabled,
            seen = params.seen
        )
    }

    data class Params(
        val from: LocalDateTime,
        val to: LocalDateTime,
        val enabled:Boolean,
        val seen:Boolean
    )
}