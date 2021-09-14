package br.com.amd.medsalarm.presentation.mappers

import br.com.amd.medsalarm.domain.model.RepeatingInterval
import br.com.amd.medsalarm.presentation.model.RepeatingIntervalVO

fun RepeatingInterval.toPresenter(): RepeatingIntervalVO {
    return when(this) {
        RepeatingInterval.CUSTOM -> RepeatingIntervalVO.CUSTOM
        RepeatingInterval.FOUR -> RepeatingIntervalVO.FOUR
        RepeatingInterval.SIX -> RepeatingIntervalVO.SIX
        RepeatingInterval.EIGHT -> RepeatingIntervalVO.EIGHT
        RepeatingInterval.TWELVE -> RepeatingIntervalVO.TWELVE
    }
}

fun RepeatingIntervalVO.toDomain(): RepeatingInterval {
    return when(this) {
        RepeatingIntervalVO.CUSTOM -> RepeatingInterval.CUSTOM
        RepeatingIntervalVO.FOUR -> RepeatingInterval.FOUR
        RepeatingIntervalVO.SIX -> RepeatingInterval.SIX
        RepeatingIntervalVO.EIGHT -> RepeatingInterval.EIGHT
        RepeatingIntervalVO.TWELVE -> RepeatingInterval.TWELVE
    }
}