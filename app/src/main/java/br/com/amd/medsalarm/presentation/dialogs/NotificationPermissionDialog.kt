package br.com.amd.medsalarm.presentation.dialogs

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import br.com.amd.medsalarm.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationPermissionDialog(
    permissionState: PermissionState,
    isOpen: Boolean,
    onDismissRequest: () -> Unit
) {
    var text = stringResource(id = R.string.notification_permission_dialog_text)
    var confirmationText = stringResource(id = R.string.notification_permission_dialog_confirm)
    var dismissText = stringResource(id = R.string.notification_permission_dialog_cancel)
    var onConfirmAction = {
        permissionState.launchPermissionRequest()
    }

    val context = LocalContext.current
    if (permissionState.status.shouldShowRationale) {
        text = stringResource(id = R.string.notification_rationale_dialog_text)
        confirmationText = stringResource(id = R.string.notification_rationale_dialog_confirm)
        dismissText = stringResource(id = R.string.notification_rationale_dialog_cancel)
        onConfirmAction = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val intent = Intent().apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            }
        }
    }

    val arguments = DialogArguments(
        title = stringResource(id = R.string.notification_permission_dialog_title),
        text = text,
        confirmationText = confirmationText,
        dismissText = dismissText,
        onConfirmAction = {
            onConfirmAction()
            onDismissRequest()
        },
        onDismissAction = {
            onDismissRequest()
        }
    )

    MedsAlarmDialog(arguments = arguments, isOpen = isOpen)
}