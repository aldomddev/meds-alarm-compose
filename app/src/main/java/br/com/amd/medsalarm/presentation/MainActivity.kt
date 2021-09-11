package br.com.amd.medsalarm.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import br.com.amd.medsalarm.presentation.screens.MainScreen
import br.com.amd.medsalarm.ui.theme.MedsAlarmTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedsAlarmTheme(darkTheme = false) {
                MainScreen("Bottom app bar + FAB")
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MedsAlarmTheme {
        MainScreen("Bottom app bar + FAB")
    }
}