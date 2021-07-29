package br.com.amd.medsalarm.ui.widgets

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.amd.medsalarm.ui.model.NavigationItem

@Composable
fun BottomNavigationBar(
    navigationItems: List<NavigationItem>,
    cutoutShape: Shape? = null,
    onItemClicked: (NavigationItem) -> Unit
) {
    BottomAppBar(
        cutoutShape = cutoutShape,
        content = {
            BottomNavigation(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            ) {
                navigationItems.forEachIndexed { index, item ->
                    BottomNavigationItem(
                        label = { Text(text = stringResource(id = item.title)) },
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = stringResource(id = item.title)
                            )
                        },
                        selected = index == 0,
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

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar(
        navigationItems = listOf(NavigationItem.TodayMeds, NavigationItem.MyMeds),
        onItemClicked = { }
    )
}