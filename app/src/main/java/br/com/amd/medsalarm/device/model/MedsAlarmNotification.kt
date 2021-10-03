package br.com.amd.medsalarm.device.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import br.com.amd.medsalarm.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class MedsAlarmNotification(
    val id: Int = 0,
    val medication: String,
    val description: String = "",
    @DrawableRes val icon: Int = R.drawable.ic_medication
): Parcelable
