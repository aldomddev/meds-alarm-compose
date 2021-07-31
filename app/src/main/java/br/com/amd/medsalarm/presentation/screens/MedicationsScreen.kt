package br.com.amd.medsalarm.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MedicationsScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Medications")
    }
}