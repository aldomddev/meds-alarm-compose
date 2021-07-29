package br.com.amd.medsalarm

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import br.com.amd.medsalarm.ui.model.NavigationItem
import br.com.amd.medsalarm.ui.theme.MedsAlarmTheme
import br.com.amd.medsalarm.ui.widgets.BottomNavigationBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedsAlarmTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val fabShape = RoundedCornerShape(50)

    Scaffold(
        topBar = { GetTopAppBar() },
        floatingActionButton = { GetFloatingActionButton(fabShape) },
        bottomBar = { GetBottomBar(fabShape) },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
    ) {

    }
}

@Composable
fun GetBottomBar(fabShape: RoundedCornerShape) {
    BottomNavigationBar(
        navigationItems = listOf(NavigationItem.TodayMeds, NavigationItem.MyMeds),
        cutoutShape = fabShape,
        onItemClicked = { }
    )
}

@Composable
fun GetFloatingActionButton(fabShape: Shape) {
    FloatingActionButton(
        onClick = { },
        shape = fabShape,
    ) {
        Icon(Icons.Filled.Add,"")
    }
}

@Composable
fun GetTopAppBar() {
    TopAppBar(
        title = {
            Text(text = "Bottom app bar + FAB")
        },
        navigationIcon = {
            IconButton(
                onClick = { }
            ) {
                Icon(Icons.Filled.Menu, contentDescription = "")
            }
        },
        actions = {
            Icon(Icons.Filled.Notifications, contentDescription = "")
        },
        elevation = AppBarDefaults.TopAppBarElevation
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MedsAlarmTheme {
        MainScreen()
    }
}