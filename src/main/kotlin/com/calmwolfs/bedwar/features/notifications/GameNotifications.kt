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
    private val config get() = BedWarMod.feature.notifications.gameNotifications

    //todo make it say "You" instead of your username
    @SubscribeEvent
    fun onKill(event: KillEvent) {
        if (!config.enabled) return
        if (event.killer in TeamStatus.currentTeamMembers || event.killer == HypixelUtils.currentName) {
            val message = formatNotification(listOf("§3${event.killer} §agot a kill!"))
            NotificationUtils.displayNotification(message, null, config.sound, config.displayLength.toDouble())
        }
    }

    @SubscribeEvent
    fun onFinal(event: FinalKillEvent) {
        if (!config.enabled) return
        if (event.killer in TeamStatus.currentTeamMembers || event.killer == HypixelUtils.currentName) {
            val message = formatNotification(listOf("§3${event.killer} §agot a final kill!"))
            NotificationUtils.displayNotification(message, null, config.sound, config.displayLength.toDouble())
        }
    }

    @SubscribeEvent
    fun onBed(event: BedBreakEvent) {
        if (!config.enabled) return
        if (event.player in TeamStatus.currentTeamMembers || event.player == HypixelUtils.currentName) {
            val message = formatNotification(listOf("§3${event.player} §abroke a bed!"))
            NotificationUtils.displayNotification(message, null, config.sound, config.displayLength.toDouble())
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