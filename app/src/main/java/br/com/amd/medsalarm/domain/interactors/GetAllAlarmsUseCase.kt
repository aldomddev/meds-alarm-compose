package br.com.amd.medsalarm.domain.interactors

import br.com.amd.medsalarm.domain.model.MedsAlarm
import br.com.amd.medsalarm.domain.repository.MedsAlarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAlarmsUseCase @Inject constructor(
    private val alarmsRepository: MedsAlarmRepository
) {
    suspend operator fun invoke(): Flow<List<MedsAlarm>> {
        return alarmsRepository.observesAll()
    }
}