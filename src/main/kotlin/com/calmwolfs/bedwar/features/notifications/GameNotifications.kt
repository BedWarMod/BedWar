package com.calmwolfs.bedwar.features.notifications

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.events.bedwars.BedBreakEvent
import com.calmwolfs.bedwar.events.bedwars.FinalKillEvent
import com.calmwolfs.bedwar.events.bedwars.KillEvent
import com.calmwolfs.bedwar.features.team.TeamStatus
import com.calmwolfs.bedwar.utils.HypixelUtils
import com.calmwolfs.bedwar.utils.gui.NotificationUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object GameNotifications {
    private val gameConfig get() = BedWarMod.feature.notifications.gameNotifications
    private val deathConfig get() = BedWarMod.feature.notifications.deathNotifications

    @SubscribeEvent
    fun onKill(event: KillEvent) {
        if (isEnabled(gameConfig.enabled, event.killer)) {
            val message = formatNotification(listOf("§3${event.killer} §agot a kill!"))
            NotificationUtils.displayNotification(message, null, gameConfig.sound, gameConfig.displayLength.toDouble())
        }
        if (isEnabled(deathConfig.enabled, event.killed)) {
            val message = formatNotification(listOf("§3${event.killed} §ewas killed!"))
            NotificationUtils.displayNotification(message, null, deathConfig.sound, deathConfig.displayLength.toDouble())
        }
    }

    @SubscribeEvent
    fun onFinal(event: FinalKillEvent) {
        if (isEnabled(gameConfig.enabled, event.killer)) {
            val message = formatNotification(listOf("§3${event.killer} §agot a final kill!"))
            NotificationUtils.displayNotification(message, null, gameConfig.sound, gameConfig.displayLength.toDouble())
        }
        if (isEnabled(deathConfig.enabled, event.killed)) {
            val message = formatNotification(listOf("§3${event.killed} §ewas final killed!"))
            NotificationUtils.displayNotification(message, null, deathConfig.sound, deathConfig.displayLength.toDouble())
        }
    }

    @SubscribeEvent
    fun onBed(event: BedBreakEvent) {
        if (isEnabled(gameConfig.enabled, event.player)) {
            val message = formatNotification(listOf("§3${event.player} §abroke a bed!"))
            NotificationUtils.displayNotification(message, null, gameConfig.sound, gameConfig.displayLength.toDouble())
        }
    }

    private fun formatNotification(message: List<String>): List<String> {
        var baseList = listOf(
            "§a§lTeam")
        if (message.isNotEmpty()) {
            baseList = baseList + message
        }
        return baseList
    }

    private fun isEnabled(config: Boolean, player: String): Boolean {
        if (!gameConfig.enabled) return false
        if (player == HypixelUtils.currentName) return false
        return player in TeamStatus.currentTeamMembers
    }
}