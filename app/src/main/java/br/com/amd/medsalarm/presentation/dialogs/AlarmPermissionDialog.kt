package br.com.amd.medsalarm.presentation.dialogs

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import br.com.amd.medsalarm.R


@Composable
fun AlarmPermissionDialog(
    context: Context,
    isOpen: Boolean,
    onDismissRequest: () -> Unit
) {
    val arguments = DialogArguments(
        title = stringResource(id = R.string.alarm_permission_dialog_title),
        text = stringResource(id = R.string.alarm_permission_dialog_text),
        confirmationText = stringResource(id = R.string.permission_dialog_confirm),
        dismissText = stringResource(id = R.string.permission_dialog_cancel),
        onConfirmAction = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val intent = Intent().apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
                onDismissRequest()
            }
        },
        onDismissAction = {
            onDismissRequest()
        }
    )

    MedsAlarmDialog(arguments = arguments, isOpen = isOpen)
}