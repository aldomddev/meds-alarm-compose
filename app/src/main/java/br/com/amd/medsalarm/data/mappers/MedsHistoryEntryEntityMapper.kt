package br.com.amd.medsalarm.data.mappers

import br.com.amd.medsalarm.data.model.MedsHistoryEntryEntity
import br.com.amd.medsalarm.domain.model.MedsHistoryEntry

fun MedsHistoryEntryEntity.toDomain(): MedsHistoryEntry {
    return MedsHistoryEntry(
        id = id,
        firedOn = firedOn,
        acknowledged = acknowledged,
        acknowledgedOn = acknowledgedOn
    )
}

fun List<MedsHistoryEntryEntity>.toDomain(): List<MedsHistoryEntry> = map { it.toDomain() }