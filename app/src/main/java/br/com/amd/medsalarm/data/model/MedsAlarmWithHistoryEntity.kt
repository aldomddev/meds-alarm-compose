package br.com.amd.medsalarm.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class MedsAlarmWithHistoryEntity(
    @Embedded val alarm: MedsAlarmEntity,
    @Relation(parentColumn = "id", entityColumn = "id")
    val history: List<MedsHistoryEntryEntity>
)
