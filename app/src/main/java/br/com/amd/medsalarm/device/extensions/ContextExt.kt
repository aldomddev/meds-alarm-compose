package br.com.amd.medsalarm.device.extensions

import android.content.Context
import android.hardware.display.DisplayManager
import android.view.Display

fun Context?.isScreenOn(): Boolean {
    if (this == null) return false

    val displayManager = this.getSystemService(Context.DISPLAY_SERVICE) as? DisplayManager
        ?: return false

    var screenOn = false
    for (display in displayManager.displays) {
        if (display.state != Display.STATE_OFF) {
            screenOn = true
        }
    }
    return screenOn
}