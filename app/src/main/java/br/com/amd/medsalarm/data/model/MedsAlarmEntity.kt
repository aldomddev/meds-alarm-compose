package br.com.amd.medsalarm.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.amd.medsalarm.domain.model.RepeatingInterval
import br.com.amd.medsalarm.domain.model.RepeatingIntervalUnit
import java.time.LocalDateTime

@Entity(tableName = "meds_alarm")
data class MedsAlarmEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")  val id: Int = 0,
    @NonNull @ColumnInfo(name = "medication") val medication: String,
    @NonNull @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "starts_on") val startsOn: LocalDateTime? = null,
    @ColumnInfo(name = "ends_on") val endsOn: LocalDateTime? = null,
    @ColumnInfo(name = "next") val next: LocalDateTime? = null,
    @NonNull @ColumnInfo(name = "repeating_interval") val repeatingInterval: RepeatingInterval = RepeatingInterval.EIGHT,
    @NonNull @ColumnInfo(name = "repeating_interval_unit") val repeatingIntervalUnit: RepeatingIntervalUnit = RepeatingIntervalUnit.HOUR,
    @NonNull @ColumnInfo(name = "enabled") val enabled: Boolean = true,
    @NonNull @ColumnInfo(name = "seen") val seen: Boolean = false
)
