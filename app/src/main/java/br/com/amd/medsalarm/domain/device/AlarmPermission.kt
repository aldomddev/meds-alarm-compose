package br.com.amd.medsalarm.domain.device

interface AlarmPermission {
    fun hasExactAlarmPermission(): Boolean
}