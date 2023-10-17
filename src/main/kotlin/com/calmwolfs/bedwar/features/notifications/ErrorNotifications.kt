package com.calmwolfs.bedwar.features.notifications

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.utils.gui.NotificationUtils
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack

object ErrorNotifications {
    private val config get() = BedWarMod.feature.notifications.modErrors

    fun repoMatchError(errorType: String) {
        sendError(formatError("$errorType Match error",
            listOf(
                "§aThe sooner this is reported",
                "§aThe more accurate your " +
                "§amatch stats will be!")
            )
        )
    }

    fun repoError() {
        val brokenConstants = BedWarMod.repo.unsuccessfulConstants.joinToString(", ")

        sendError(formatError("Repo Error!",
            listOf(
                "§eBroken constants:",
                "§e$brokenConstants")
            )
        )
    }

    fun error(errorMessage: String) {
        sendError(formatError(errorMessage))
    }

    private fun sendError(error: List<String>) {
        if (!config.enabled) return
        val item = ItemStack(Blocks.barrier)
        NotificationUtils.displayNotification(error, item, config.sound, config.displayLength.toDouble())
    }

    private fun formatError(errorMessage: String, extraLines: List<String> = listOf()): List<String> {
        var baseList = listOf(
            "§cBedWar Mod encountered an error!",
            "§e$errorMessage",
            "§ePlease report this on the GitHub",
            "§eSo that it can be fixed!"
        )
        if (extraLines.isNotEmpty()) {
            baseList = baseList + extraLines
        }
        return baseList
    }
}