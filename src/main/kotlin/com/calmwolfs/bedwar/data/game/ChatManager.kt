package com.calmwolfs.bedwar.data.game

import com.calmwolfs.bedwar.events.ModChatEvent
import com.calmwolfs.bedwar.utils.StringUtils.stripResets
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ChatManager {
    @SubscribeEvent(receiveCanceled = true)
    fun onChatReceive(event: ClientChatReceivedEvent) {
        if (event.type.toInt() == 2) return

        val original = event.message
        val message = original.formattedText.stripResets()

        if (message.startsWith("§f{\"server\":\"")) return

        val chatEvent = ModChatEvent(message, original)
        chatEvent.postAndCatch()
        val blockReason = chatEvent.blockedReason

        if (blockReason != "") {
            event.isCanceled = true
        }
    }
}