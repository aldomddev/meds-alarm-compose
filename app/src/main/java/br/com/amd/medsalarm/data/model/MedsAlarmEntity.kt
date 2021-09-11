package br.com.amd.medsalarm.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "medsAlarm")
data class MedsAlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val medication: String,
    val description: String = "",
    val startsOn: LocalDateTime,
    val endsOn: LocalDateTime? = null,
    val repeatingInterval: RepeatingInterval = RepeatingInterval.EIGHT,
    val repeatingIntervalUnit: RepeatingIntervalUnit = RepeatingIntervalUnit.HOUR,
    val seen: Boolean = false
)
