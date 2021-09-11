package br.com.amd.medsalarm.presentation.screens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Shape
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.amd.medsalarm.presentation.model.NavigationItem
import br.com.amd.medsalarm.ui.widgets.BottomNavigationBar
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun MainScreen(title: String) {
    val fabShape = RoundedCornerShape(50)
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(title = title, scaffoldState = scaffoldState) },
        floatingActionButton = {
            GetFloatingActionButton(
                navController = navController,
                fabShape = fabShape,
                onAddButtonClicked = { navController.navigate(NavigationItem.MedsDetail.route) }
            )
        },
        bottomBar = { BottomBar(navController = navController, fabShape = fabShape) },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        drawerContent = { Text("Empty drawer") },
        content = { Navigator(navController = navController) }
    )
}

@Composable
private fun showBottomBar(navController: NavController): Boolean {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    return navBackStackEntry.value?.destination?.route != NavigationItem.MedsDetail.route
}

@ExperimentalMaterialApi
@Composable
private fun Navigator(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.TodayMeds.route) {
        composable(NavigationItem.TodayMeds.route) {
            TodayMedsScreen(viewModel = hiltViewModel())
        }

        composable(NavigationItem.MyMeds.route) {
            MedicationsScreen()
        }

        composable(NavigationItem.MedsDetail.route) {
            MedicationDetailScreen()
        }
    }
}

@Composable
private fun TopBar(
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
private fun BottomBar(
    navController: NavController,
    fabShape: RoundedCornerShape
) {
    if (showBottomBar(navController)) {
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
}

@Composable
private fun GetFloatingActionButton(
    navController: NavController,
    fabShape: Shape,
    onAddButtonClicked: () -> Unit
) {
    if (showBottomBar(navController)) {
        FloatingActionButton(
            onClick = { onAddButtonClicked.invoke() },
            shape = fabShape,
        ) {
            Icon(Icons.Filled.Add,"")
        }
    }
}