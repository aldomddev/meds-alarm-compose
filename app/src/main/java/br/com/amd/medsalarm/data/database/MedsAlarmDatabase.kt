package br.com.amd.medsalarm.data.database

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import br.com.amd.medsalarm.data.converters.Converters
import br.com.amd.medsalarm.data.dao.MedsAlarmDao
import br.com.amd.medsalarm.data.model.MedsAlarmEntity
import br.com.amd.medsalarm.data.model.MedsHistoryEntryEntity

@Database(
    entities = [
        MedsAlarmEntity::class,
        MedsHistoryEntryEntity::class
    ],
    exportSchema = true,
    version = 2,
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
            spec = MedsAlarmDatabase.MedsAlarmMigration::class
        )
    ]
)
@TypeConverters(Converters::class)
abstract class MedsAlarmDatabase : RoomDatabase() {
    @DeleteColumn(tableName = "meds_alarm", columnName = "seen")
    class MedsAlarmMigration : AutoMigrationSpec

    abstract fun medsAlarmDao(): MedsAlarmDao
}