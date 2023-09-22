package com.calmwolfs.bedwar.utils

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.utils.StringUtils.removeColour
import net.minecraft.client.Minecraft
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import java.util.*
import kotlin.time.Duration

object ModUtils {



    fun chat(message: String) {
        internalChat(message)
    }

    fun warning(message: String) {
        internalChat("§cWarning! $message")
    }

    fun error(message: String) {
        println("error: '$message'")
        internalChat("§c$message")
    }

    private fun internalChat(message: String): Boolean {
        val minecraft = Minecraft.getMinecraft()
        if (minecraft == null) {
            BedWarMod.consoleLog(message.removeColour())
            return false
        }

        val thePlayer = minecraft.thePlayer
        if (thePlayer == null) {
            BedWarMod.consoleLog(message.removeColour())
            return false
        }

        thePlayer.addChatMessage(ChatComponentText(message))
        return true
    }

    fun clickableChat(message: String, command: String) {
        val text = ChatComponentText(message)
        val fullCommand = "/" + command.removePrefix("/")
        text.chatStyle.chatClickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, fullCommand)
        text.chatStyle.chatHoverEvent =
            HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponentText("§eExecute $fullCommand"))
        Minecraft.getMinecraft().thePlayer.addChatMessage(text)
    }

    fun runDelayed(duration: Duration, runnable: () -> Unit) {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                runnable()
            }
        }, duration.inWholeMilliseconds)
    }
}