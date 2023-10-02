package com.calmwolfs.bedwar.features.chat

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.events.game.PlayerChatEvent
import com.calmwolfs.bedwar.utils.BedwarsUtils
import com.calmwolfs.bedwar.utils.HypixelUtils
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class PlayerChatClick {
    @SubscribeEvent
    fun onPlayerMessage(event: PlayerChatEvent) {
        if (event.sender == HypixelUtils.currentName) return
        if (!BedwarsUtils.inBedwarsArea) return
        if (BedWarMod.feature.chat.playerStats.clickName) {
            if (event.component.siblings.isEmpty()) return
            val lastComponent = event.component.siblings.last()
            if (lastComponent.chatStyle.chatClickEvent != null) {
                if (!lastComponent.chatStyle.chatClickEvent.value.startsWith("/socialoptions")) return
            }
            lastComponent.chatStyle.chatClickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bws ${event.sender}")
            lastComponent.chatStyle.chatHoverEvent = HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                ChatComponentText("§aView the stats of §3${event.sender}")
            )
        }
    }
}