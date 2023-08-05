package br.com.amd.medsalarm.presentation.dialogs

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import br.com.amd.medsalarm.ui.theme.MedsAlarmTheme

@Composable
fun MedsAlarmDialog(
    arguments: DialogArguments,
    isOpen: Boolean,
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = arguments.onDismissAction,
            title = { Text(text = arguments.title) },
            text = { Text(text = arguments.text) },
            confirmButton = {
                Button(onClick = arguments.onConfirmAction) {
                    Text(text = arguments.confirmationText)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = arguments.onDismissAction) {
                    Text(text = arguments.dismissText)
                }
            }
        )
    }
}

data class DialogArguments(
    val title: String,
    val text: String,
    val confirmationText: String,
    val dismissText: String,
    val onConfirmAction: () -> Unit,
    val onDismissAction: () -> Unit
)

@Preview
@Composable
fun DialogPreview() {
    MedsAlarmTheme {
        val args = DialogArguments(
            title = "Wait!",
            text = "Are you sure?",
            confirmationText = "Of course!",
            dismissText = "No, close.",
            onConfirmAction = {},
            onDismissAction = {}
        )

        MedsAlarmDialog(arguments = args, isOpen = true)
    }
}