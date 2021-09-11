package br.com.amd.medsalarm.domain.model

enum class RepeatingInterval(val interval: Int) {
    CUSTOM(-1),
    FOUR(4),
    SIX(6),
    EIGHT(8),
    TWELVE(12)
}