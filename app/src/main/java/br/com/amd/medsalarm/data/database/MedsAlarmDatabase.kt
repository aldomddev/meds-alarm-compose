package br.com.amd.medsalarm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.amd.medsalarm.data.converters.Converters
import br.com.amd.medsalarm.data.dao.MedsAlarmDao
import br.com.amd.medsalarm.data.model.MedsAlarmEntity

@Database(
    entities = [MedsAlarmEntity::class],
    exportSchema = true,
    version = 1
)
@TypeConverters(Converters::class)
abstract class MedsAlarmDatabase: RoomDatabase() {
    abstract fun medsAlarmDao(): MedsAlarmDao
}