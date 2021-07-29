package br.com.amd.medsalarm.ui.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import br.com.amd.medsalarm.R

sealed class NavigationItem(
    @StringRes val route: Int,
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {
    object TodayMeds : NavigationItem(
        route = R.string.navigation_today_meds_route,
        title = R.string.navigation_today_meds_title,
        icon = R.drawable.ic_today
    )

    object MyMeds : NavigationItem(
        route = R.string.navigation_my_meds_route,
        title = R.string.navigation_route_my_meds_title,
        icon = R.drawable.ic_medication
    )
}
