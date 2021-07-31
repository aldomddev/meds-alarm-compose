package br.com.amd.medsalarm.presentation.screens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Shape
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.amd.medsalarm.presentation.model.NavigationItem
import br.com.amd.medsalarm.ui.widgets.BottomNavigationBar
import kotlinx.coroutines.launch

@Composable
fun MainScreen(title: String) {
    val fabShape = RoundedCornerShape(50)
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { GetTopAppBar(title = title, scaffoldState = scaffoldState) },
        floatingActionButton = { GetFloatingActionButton(fabShape) },
        bottomBar = { GetBottomBar(navController = navController, fabShape = fabShape) },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        drawerContent = { Text("Empty drawer") },
        content = { Navigator(navController = navController) }
    )
}

@Composable
fun Navigator(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.TodayMeds.route) {
        composable(NavigationItem.TodayMeds.route) {
            TodayMedsScreen()
        }

        composable(NavigationItem.MyMeds.route) {
            MedicationsScreen()
        }
    }
}

@Composable
private fun GetTopAppBar(
    title: String,
    scaffoldState: ScaffoldState
) {
    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(
                content = {
                    Icon(Icons.Filled.Menu, contentDescription = "")
                },
                onClick = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            )
        },
        actions = {
            IconButton(
                content = {
                    Icon(Icons.Filled.Notifications, contentDescription = "")
                },
                onClick = { }
            )
        },
        elevation = AppBarDefaults.TopAppBarElevation
    )
}

@Composable
private fun GetBottomBar(
    navController: NavController,
    fabShape: RoundedCornerShape
) {
    BottomNavigationBar(
        navController = navController,
        navigationItems = listOf(NavigationItem.TodayMeds, NavigationItem.MyMeds),
        cutoutShape = fabShape,
        onItemClicked = { destination ->
            navController.navigate(destination.route) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                navController.graph.startDestinationRoute?.let { route ->
                    popUpTo(route) { saveState = true }
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }
    )
}

@Composable
private fun GetFloatingActionButton(fabShape: Shape) {
    FloatingActionButton(
        onClick = { },
        shape = fabShape,
    ) {
        Icon(Icons.Filled.Add,"")
    }
}