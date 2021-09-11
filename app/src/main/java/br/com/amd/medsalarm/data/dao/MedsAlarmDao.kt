package br.com.amd.medsalarm.data.dao

import androidx.room.*
import br.com.amd.medsalarm.data.model.MedsAlarmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedsAlarmDao {
    @Query("SELECT * FROM medsAlarm ORDER BY datetime(startsOn)")
    fun all(): Flow<List<MedsAlarmEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveOrUpdate(alarm: MedsAlarmEntity)

    @Delete
    fun delete(alarm: MedsAlarmEntity)
}