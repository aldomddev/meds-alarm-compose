package br.com.amd.medsalarm.presentation.mappers

import br.com.amd.medsalarm.domain.model.MedsHistoryEntry
import br.com.amd.medsalarm.presentation.model.MedsHistoryEntryVO

fun MedsHistoryEntry.toPresenter(): MedsHistoryEntryVO {
    return MedsHistoryEntryVO(
        id = id,
        firedOn = firedOn,
        acknowledged = acknowledged,
        acknowledgedOn = acknowledgedOn
    )
}

fun List<MedsHistoryEntry>.toPresenter(): List<MedsHistoryEntryVO> = map { it.toPresenter() }