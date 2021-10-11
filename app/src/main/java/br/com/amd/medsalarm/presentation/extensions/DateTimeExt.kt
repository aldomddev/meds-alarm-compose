package br.com.amd.medsalarm.presentation.extensions

import br.com.amd.medsalarm.presentation.model.TimeToNextAlarmVO
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun LocalDate?.toFormattedString(): String {
    return if (this != null) {
        this.format(DateTimeFormatter.ISO_LOCAL_DATE)
    } else {
        "--/--/--"
    }
}

fun LocalTime?.toFormattedString(): String {
    return if (this != null) {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        this.format(formatter)
    } else {
        "--:--"
    }
}

fun LocalDateTime?.toTimeUntilNextAlarmFrom(fromDateTime: LocalDateTime): TimeToNextAlarmVO {
    return this?.let { dateTime ->
        val isToday = dateTime.dayOfMonth == fromDateTime.dayOfMonth && dateTime.month == fromDateTime.month && dateTime.year == fromDateTime.year
        val days = fromDateTime.until(dateTime, ChronoUnit.DAYS)
        val hours = fromDateTime.plusDays(days).until(dateTime, ChronoUnit.HOURS)
        val minutes = fromDateTime.plusDays(days).plusHours(hours).until(dateTime, ChronoUnit.MINUTES)
        val isTomorrow = days == 0L && fromDateTime.hour + hours >= 24

        TimeToNextAlarmVO(
            isToday = isToday,
            isTomorrow = isTomorrow,
            days = days,
            hours = hours,
            minutes = minutes
        )
    } ?: TimeToNextAlarmVO(isToday = false, isTomorrow = false, days = 0, hours = 0, minutes = 0)
}