package br.com.amd.medsalarm.domain.interactors

import br.com.amd.medsalarm.domain.repository.MedsAlarmRepository
import javax.inject.Inject

class GetAllAlarmsUseCase @Inject constructor(
    private val alarmsRepository: MedsAlarmRepository
) {
    suspend operator fun invoke() {
        alarmsRepository.observesAll()
    }
}