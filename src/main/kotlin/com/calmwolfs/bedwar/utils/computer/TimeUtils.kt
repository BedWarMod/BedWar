package com.calmwolfs.bedwar.utils.computer

object TimeUtils {
    fun formatSeconds(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60

        return when {
            hours > 10 -> String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
            hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, remainingSeconds)
            minutes > 10 -> String.format("%02d:%02d", minutes, remainingSeconds)
            else -> String.format("%d:%02d", minutes, remainingSeconds)
        }
    }
}