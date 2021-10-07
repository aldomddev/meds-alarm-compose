package br.com.amd.medsalarm.presentation.extensions

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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