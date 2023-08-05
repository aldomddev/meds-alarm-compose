package br.com.amd.medsalarm.common.extentions

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State

fun <T> MutableState<T>.toState(): State<T> = this