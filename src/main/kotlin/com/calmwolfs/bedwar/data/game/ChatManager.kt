package com.calmwolfs.bedwar.data.game

import com.calmwolfs.bedwar.events.game.GameChatEvent
import com.calmwolfs.bedwar.events.game.PlayerChatEvent
import com.calmwolfs.bedwar.utils.ChatUtils.getPlayerName
import com.calmwolfs.bedwar.utils.StringUtils.removeResets
import com.calmwolfs.bedwar.utils.StringUtils.trimWhiteSpace
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ChatManager {
    @SubscribeEvent(receiveCanceled = true)
    fun onChatReceive(event: ClientChatReceivedEvent) {
        if (event.type.toInt() == 2) return

        val original = event.message
        val message = original.formattedText.removeResets()

        if (message.startsWith("§f{\"server\":\"")) return

        val sender = message.trimWhiteSpace().getPlayerName()

        val blockedReason = if (sender == null) {
            val chatEvent = GameChatEvent(message, original)
            chatEvent.postAndCatch()
            chatEvent.blockedReason
        } else {
            val chatEvent = PlayerChatEvent(message, original, sender)
            chatEvent.postAndCatch()
            chatEvent.blockedReason
        }

        if (blockedReason != "") {
            event.isCanceled = true
        }
    }
}