package br.com.amd.medsalarm.domain.repository

import br.com.amd.medsalarm.domain.model.MedsAlarm
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface MedsAlarmRepository {
    suspend fun observesAll(): Flow<List<MedsAlarm>>
    suspend fun findNextAlarmsForPeriod(from: LocalDateTime, to: LocalDateTime, enabled: Boolean = true, seen: Boolean = true): Flow<List<MedsAlarm>>
    suspend fun getEnabled(): List<MedsAlarm>
    suspend fun getById(id: Int): MedsAlarm
    suspend fun saveOrUpdate(alarm: MedsAlarm)
    suspend fun delete(alarm: MedsAlarm)
}