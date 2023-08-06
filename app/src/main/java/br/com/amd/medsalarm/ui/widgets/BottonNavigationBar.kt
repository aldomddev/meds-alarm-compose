package br.com.amd.medsalarm.ui.widgets

import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import br.com.amd.medsalarm.presentation.navigation.Destination

@Composable
fun BottomNavigationBar(
    navController: NavController,
    destinations: List<Destination>,
    cutoutShape: Shape? = null,
    onItemClicked: (Destination) -> Unit
) {
    BottomAppBar(
        cutoutShape = cutoutShape,
        content = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            BottomNavigation(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            ) {
                destinations.forEach { item ->
                    BottomNavigationItem(
                        label = { Text(text = stringResource(id = item.title)) },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = stringResource(id = item.title)
                            )
                        },
                        selected = currentRoute == item.route,
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.White.copy(0.4f),
                        alwaysShowLabel = true,
                        onClick = { onItemClicked.invoke(item) }
                    )
                }
            }
        }
    )
}

//@Preview(showBackground = true)
//@Composable
//fun BottomNavigationBarPreview() {
//    BottomNavigationBar(
//        navigationItems = listOf(NavigationItem.TodayMeds, NavigationItem.MyMeds),
//        onItemClicked = { }
//    )
//}