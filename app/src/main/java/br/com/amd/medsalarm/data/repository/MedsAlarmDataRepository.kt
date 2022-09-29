package br.com.amd.medsalarm.data.repository

import br.com.amd.medsalarm.data.dao.MedsAlarmDao
import br.com.amd.medsalarm.data.mappers.toDomain
import br.com.amd.medsalarm.data.mappers.toEntity
import br.com.amd.medsalarm.domain.model.MedsAlarm
import br.com.amd.medsalarm.domain.model.MedsAlarmWithHistory
import br.com.amd.medsalarm.domain.repository.MedsAlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import java.time.LocalDateTime
import javax.inject.Inject

class MedsAlarmDataRepository @Inject constructor(
    private val dao: MedsAlarmDao
): MedsAlarmRepository {
    override suspend fun observesAll(): Flow<List<MedsAlarm>> {
        return dao.all().transform { alarm -> emit(alarm.toDomain()) }
    }

    override suspend fun findNextAlarmsForPeriod(
        from: LocalDateTime,
        to: LocalDateTime,
        enabled: Boolean,
        seen: Boolean
    ): Flow<List<MedsAlarm>> {
        return dao.findNextAlarmsForPeriod(from, to, enabled).transform { alarm -> emit(alarm.toDomain()) }
    }

    override suspend fun getEnabled(): List<MedsAlarm> {
        return dao.getEnabledAlarms().toDomain()
    }

    override suspend fun getById(id: Int): MedsAlarm {
        return dao.getAlarmById(id).toDomain()
    }

    override suspend fun getAlarmsWithHistory(): List<MedsAlarmWithHistory> {
        return dao.getAlarmsWithHistory().toDomain()
    }

    override suspend fun saveOrUpdate(alarm: MedsAlarm) {
        dao.saveOrUpdate(alarm.toEntity())
    }

    override suspend fun delete(alarm: MedsAlarm) {
        dao.delete(alarm.toEntity())
    }
}