package com.calmwolfs.bedwar.features.notifications

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.events.PlayerJoinPartyEvent
import com.calmwolfs.bedwar.utils.gui.NotificationUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object PartyNotifications {
    private val config get() = BedWarMod.feature.notifications.partyNotifications

    @SubscribeEvent
    fun onPartyJoin(event: PlayerJoinPartyEvent) {
        val message = formatNotification(listOf("§3${event.player} §ajoined your party!"))
        NotificationUtils.displayNotification(message, null, config.sound, config.displayLength.toDouble())
    }

    private fun formatNotification(message: List<String>): List<String> {
        var baseList = listOf(
            "§a§lParty")
        if (message.isNotEmpty()) {
            baseList = baseList + message
        }
        return baseList
    }
}