package br.com.amd.medsalarm.domain.model

import java.time.LocalDateTime

data class MedsHistoryEntry(
    val id: Int = 0,
    val firedOn: LocalDateTime,
    val acknowledged: Boolean,
    val acknowledgedOn: LocalDateTime
)
