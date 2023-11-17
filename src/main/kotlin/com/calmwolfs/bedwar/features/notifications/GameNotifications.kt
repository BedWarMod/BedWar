package com.calmwolfs.bedwar.features.notifications

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.events.bedwars.BedBreakEvent
import com.calmwolfs.bedwar.events.bedwars.FinalKillEvent
import com.calmwolfs.bedwar.events.bedwars.KillEvent
import com.calmwolfs.bedwar.features.team.TeamStatus
import com.calmwolfs.bedwar.utils.gui.NotificationUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object GameNotifications {
    private val gameConfig get() = BedWarMod.feature.notifications.gameNotifications
    private val deathConfig get() = BedWarMod.feature.notifications.deathNotifications

    //todo make it say "You" instead of your username
    @SubscribeEvent
    fun onKill(event: KillEvent) {
        if (gameConfig.enabled && event.killer in TeamStatus.currentTeamMembers) {
            val message = formatNotification(listOf("§3${event.killer} §agot a kill!"))
            NotificationUtils.displayNotification(message, null, gameConfig.sound, gameConfig.displayLength.toDouble())
        }
        if (deathConfig.enabled && event.killed in TeamStatus.currentTeamMembers) {
            val message = formatNotification(listOf("§3${event.killed} §ewas killed!"))
            NotificationUtils.displayNotification(message, null, deathConfig.sound, deathConfig.displayLength.toDouble())
        }
    }

    @SubscribeEvent
    fun onFinal(event: FinalKillEvent) {
        if (gameConfig.enabled && event.killer in TeamStatus.currentTeamMembers) {
            val message = formatNotification(listOf("§3${event.killer} §agot a final kill!"))
            NotificationUtils.displayNotification(message, null, gameConfig.sound, gameConfig.displayLength.toDouble())
        }
        if (deathConfig.enabled && event.killed in TeamStatus.currentTeamMembers) {
            val message = formatNotification(listOf("§3${event.killed} §ewas final killed!"))
            NotificationUtils.displayNotification(message, null, deathConfig.sound, deathConfig.displayLength.toDouble())
        }
    }

    @SubscribeEvent
    fun onBed(event: BedBreakEvent) {
        if (gameConfig.enabled && event.player in TeamStatus.currentTeamMembers) {
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
}