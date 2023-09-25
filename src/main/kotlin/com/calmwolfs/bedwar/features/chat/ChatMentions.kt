package com.calmwolfs.bedwar.features.chat

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.events.PlayerChatEvent
import com.calmwolfs.bedwar.utils.BedwarsUtils
import com.calmwolfs.bedwar.utils.ChatUtils
import com.calmwolfs.bedwar.utils.HypixelUtils
import com.calmwolfs.bedwar.utils.SoundUtils
import com.calmwolfs.bedwar.utils.StringUtils.unformat
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ChatMentions {
    private val config get() = BedWarMod.feature.chat

    @SubscribeEvent
    fun onPlayerMessage(event: PlayerChatEvent) {
        if (event.sender == HypixelUtils.currentName) return
        if (!(BedwarsUtils.inBedwarsLobby || BedwarsUtils.inBedwarsQueue)) return
        val message = event.message
        val name = HypixelUtils.currentName
        if (!message.unformat().contains(name)) return
        if (config.chatMentions) {
            SoundUtils.playBeepSound()
        }
        if (config.chatHighlight) {
            val formattingCode = message[message.indexOf(':') - 1]
            ChatUtils.replaceFirstChatText(event.chatComponent, name, "§e$name§$formattingCode")
        }
    }
}