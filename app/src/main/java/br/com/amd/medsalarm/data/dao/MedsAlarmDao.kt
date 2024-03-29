package br.com.amd.medsalarm.data.dao

import androidx.room.*
import br.com.amd.medsalarm.data.model.MedsAlarmEntity
import br.com.amd.medsalarm.data.model.MedsAlarmWithHistoryEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface MedsAlarmDao {
    @Query("SELECT * FROM meds_alarm ORDER BY datetime(starts_on)")
    fun all(): Flow<List<MedsAlarmEntity>>

    @Query("SELECT * FROM meds_alarm WHERE next BETWEEN :from AND :to AND enabled = :enabled ORDER BY datetime(next)")
    fun findNextAlarmsForPeriod(from: LocalDateTime, to: LocalDateTime, enabled: Boolean = true): Flow<List<MedsAlarmEntity>>

    @Query("SELECT * FROM meds_alarm WHERE id = :id")
    fun getAlarmById(id: Int): MedsAlarmEntity

    @Query("SELECT * FROM meds_alarm WHERE enabled = 1")
    fun getEnabledAlarms(): List<MedsAlarmEntity>

    @Transaction
    @Query("SELECT * FROM meds_alarm")
    fun getAlarmsWithHistory(): List<MedsAlarmWithHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveOrUpdate(alarm: MedsAlarmEntity)

    @Delete
    fun delete(alarm: MedsAlarmEntity)
}