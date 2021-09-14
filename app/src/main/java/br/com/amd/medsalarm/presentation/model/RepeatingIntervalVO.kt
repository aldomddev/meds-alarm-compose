package br.com.amd.medsalarm.presentation.model

import androidx.annotation.StringRes
import br.com.amd.medsalarm.R
import br.com.amd.medsalarm.domain.model.RepeatingInterval

enum class RepeatingIntervalVO(
    val interval: Int,
    @StringRes val intervalRes: Int
) {
    CUSTOM(RepeatingInterval.CUSTOM.ordinal, R.string.repeat_custom),
    FOUR(RepeatingInterval.FOUR.ordinal, R.string.repeat_every_four_hours),
    SIX(RepeatingInterval.SIX.ordinal, R.string.repeat_every_six_hours),
    EIGHT(RepeatingInterval.EIGHT.ordinal, R.string.repeat_every_eight_hours),
    TWELVE(RepeatingInterval.TWELVE.ordinal, R.string.repeat_every_twelve_hours)
}