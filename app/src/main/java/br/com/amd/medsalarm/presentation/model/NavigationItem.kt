package br.com.amd.medsalarm.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import br.com.amd.medsalarm.R

sealed class NavigationItem(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {
    object TodayMeds : NavigationItem(
        route = "TodayMeds",
        title = R.string.navigation_today_meds_title,
        icon = R.drawable.ic_today
    )

    object MyMeds : NavigationItem(
        route = "MyMeds",
        title = R.string.navigation_my_meds_title,
        icon = R.drawable.ic_medication
    )
}
