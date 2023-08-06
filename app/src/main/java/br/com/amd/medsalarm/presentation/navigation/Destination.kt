package br.com.amd.medsalarm.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import br.com.amd.medsalarm.R

sealed class Destination(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {
    object TodayMeds : Destination(
        route = "todayMeds",
        title = R.string.navigation_today_meds_title,
        icon = R.drawable.ic_today
    )

    object MyMeds : Destination(
        route = "myMeds",
        title = R.string.navigation_my_meds_title,
        icon = R.drawable.ic_medication
    )

    object MedsDetail : Destination(
        route = "medsDetail?id=",
        title = R.string.navigation_my_meds_title,
        icon = R.drawable.ic_medication
    )
}
