package br.com.amd.medsalarm.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import br.com.amd.medsalarm.presentation.screens.MainScreen
import br.com.amd.medsalarm.ui.theme.MedsAlarmTheme
import br.com.amd.medsalarm.ui.widgets.DateTimePickerDialog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedsAlarmTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen("Bottom app bar + FAB")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MedsAlarmTheme {
        MainScreen("Bottom app bar + FAB")
    }
}