package br.com.amd.medsalarm.presentation.screens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import br.com.amd.medsalarm.presentation.mappers.toDomain
import br.com.amd.medsalarm.presentation.model.MedsAlarmListState
import br.com.amd.medsalarm.presentation.navigation.Destination
import br.com.amd.medsalarm.presentation.viewmodels.MedicationDetailViewModel
import br.com.amd.medsalarm.presentation.viewmodels.MedsViewModel
import br.com.amd.medsalarm.presentation.viewmodels.TodayMedsViewModel
import br.com.amd.medsalarm.ui.widgets.BottomNavigationBar
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun MainScreen(title: String) {
    val fabShape = RoundedCornerShape(percent = 50)
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(title = title, scaffoldState = scaffoldState) },
        floatingActionButton = {
            GetFloatingActionButton(
                navController = navController,
                fabShape = fabShape,
                onAddButtonClicked = { navController.navigate(Destination.MedsDetail.route) }
            )
        },
        bottomBar = { BottomBar(navController = navController, fabShape = fabShape) },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        drawerContent = { Text("Empty drawer") },
        content = { Navigator(navController = navController) }
    )
}

@ExperimentalMaterialApi
@Composable
private fun Navigator(navController: NavHostController) {
    NavHost(navController, startDestination = Destination.TodayMeds.route) {

        composable(Destination.TodayMeds.route) {
            val todayMedsViewModel: TodayMedsViewModel = hiltViewModel()

            val alarmsViewState = todayMedsViewModel.viewState.collectAsStateWithLifecycle()

            MedsListScreen(
                viewState = alarmsViewState.value,
                onEditItemRequest = { item ->
                    navController.navigate(Destination.MedsDetail.route.plus("${item.id}"))
                },
                onDeleteItemRequest = { item ->
                    todayMedsViewModel.removeAlarm(item.toDomain())
                }
            )
        }

        composable(Destination.MyMeds.route) {
            val medsViewModel: MedsViewModel = hiltViewModel()
            val alarmsViewState = medsViewModel.viewState.collectAsStateWithLifecycle()

            MedsListScreen(
                viewState = alarmsViewState.value,
                onEditItemRequest = { item ->
                    navController.navigate(Destination.MedsDetail.route.plus("${item.id}"))
                },
                onDeleteItemRequest = { item ->
                    medsViewModel.removeAlarm(item.toDomain())
                }
            )
        }

        composable(
            route = Destination.MedsDetail.route.plus("{id}"),
            arguments = listOf(navArgument(name = "id") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) {
            val medicationDetailViewModel: MedicationDetailViewModel = hiltViewModel()

            MedicationDetailScreen(
                viewModel = medicationDetailViewModel,
                onSaveClicked = { navController.navigateUp() },
                onCancelClicked = { navController.navigateUp() }
            )
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
            destinations = listOf(Destination.TodayMeds, Destination.MyMeds),
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

@Composable
private fun showBottomBar(navController: NavController): Boolean {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    return navBackStackEntry.value?.destination?.route?.contains(Destination.MedsDetail.route) == false
}