package br.com.amd.medsalarm.domain.repository

import br.com.amd.medsalarm.domain.model.MedsAlarm
import kotlinx.coroutines.flow.Flow

interface MedsAlarmRepository {
    suspend fun observesAll(): Flow<List<MedsAlarm>>
    suspend fun saveOrUpdate(alarm: MedsAlarm)
    suspend fun delete(alarm: MedsAlarm)
}