package br.com.amd.medsalarm.presentation

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import br.com.amd.medsalarm.ui.theme.MedsAlarmTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FullScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        showWhenLockedAndTurnScreenOn()
        super.onCreate(savedInstanceState)
        setContent {
            MedsAlarmTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = MaterialTheme.colors.isLight

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons
                    )
                }
            }
        }
    }

    private fun showWhenLockedAndTurnScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
    }
}