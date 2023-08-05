package br.com.amd.medsalarm.domain.device

interface PermissionChecker {
    fun checkPermission(permission: String): Boolean
    fun hasExactAlarmPermission(): Boolean
    fun hasNotificationPermission(): Boolean
}