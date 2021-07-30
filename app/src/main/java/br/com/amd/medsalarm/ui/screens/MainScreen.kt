package br.com.amd.medsalarm.ui.screens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import br.com.amd.medsalarm.ui.model.NavigationItem
import br.com.amd.medsalarm.ui.widgets.BottomNavigationBar

@Composable
fun MainScreen(title: String) {
    val fabShape = RoundedCornerShape(50)

    Scaffold(
        topBar = { GetTopAppBar(title) },
        floatingActionButton = { GetFloatingActionButton(fabShape) },
        bottomBar = { GetBottomBar(fabShape) },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
    ) {
        TodayMedsScreen()
    }
}

@Composable
private fun GetBottomBar(fabShape: RoundedCornerShape) {
    BottomNavigationBar(
        navigationItems = listOf(NavigationItem.TodayMeds, NavigationItem.MyMeds),
        cutoutShape = fabShape,
        onItemClicked = { }
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

@Composable
private fun GetTopAppBar(title: String) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(
                onClick = { }
            ) {
                Icon(Icons.Filled.Menu, contentDescription = "")
            }
        },
        actions = {
            IconButton(
                onClick = { }
            ) {
                Icon(Icons.Filled.Notifications, contentDescription = "")
            }
        },
        elevation = AppBarDefaults.TopAppBarElevation
    )
}