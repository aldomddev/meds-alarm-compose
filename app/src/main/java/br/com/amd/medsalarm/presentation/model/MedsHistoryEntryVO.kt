package br.com.amd.medsalarm.presentation.model

import java.time.LocalDateTime

data class MedsHistoryEntryVO(
    val id: Int = 0,
    val firedOn: LocalDateTime,
    val acknowledged: Boolean,
    val acknowledgedOn: LocalDateTime
)
