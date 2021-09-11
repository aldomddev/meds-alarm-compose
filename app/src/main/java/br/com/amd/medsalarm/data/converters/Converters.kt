package br.com.amd.medsalarm.data.converters

import androidx.room.TypeConverter
import br.com.amd.medsalarm.domain.model.RepeatingInterval
import br.com.amd.medsalarm.domain.model.RepeatingIntervalUnit
import java.time.LocalDateTime

internal object Converters {
    @TypeConverter
    @JvmStatic
    fun toLocalDateTime(dateTime: String): LocalDateTime {
        return try {
            return LocalDateTime.parse(dateTime)
        } catch (error: Exception) {
            LocalDateTime.now()
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalDateTime(localDateTime: LocalDateTime): String {
        return try {
            localDateTime.toString()
        } catch (error: Exception) {
            LocalDateTime.now().toString()
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromRepeatingInterval(interval: RepeatingInterval) = interval.interval

    @TypeConverter
    @JvmStatic
    fun toRepeatingInterval(interval: Int) = RepeatingInterval.values().first { it.interval == interval }

    @TypeConverter
    @JvmStatic
    fun fromRepeatingIntervalUnit(unit: RepeatingIntervalUnit) = unit.ordinal

    @TypeConverter
    @JvmStatic
    fun toRepeatingIntervalUnit(unit: Int) = RepeatingIntervalUnit.values().first { it.ordinal == unit }
}