package br.com.amd.medsalarm.data.dao

import androidx.room.*
import br.com.amd.medsalarm.data.model.MedsAlarmEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface MedsAlarmDao {
    @Query("SELECT * FROM meds_alarm ORDER BY datetime(starts_on)")
    fun all(): Flow<List<MedsAlarmEntity>>

    @Query("SELECT * FROM meds_alarm WHERE next BETWEEN :from AND :to AND enabled = :enabled AND seen = :seen ORDER BY datetime(next)")
    fun findNextAlarmsForPeriod(from: LocalDateTime, to: LocalDateTime, enabled: Boolean = true, seen: Boolean = true): List<MedsAlarmEntity>

    @Query("SELECT * FROM meds_alarm WHERE id = :id")
    fun getAlarmById(id: Int): MedsAlarmEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveOrUpdate(alarm: MedsAlarmEntity)

    @Delete
    fun delete(alarm: MedsAlarmEntity)
}