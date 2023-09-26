package com.calmwolfs.bedwar.features.chat

import com.calmwolfs.bedwar.BedWarMod
import com.calmwolfs.bedwar.mixins.transformers.AccessorGuiNewChat
import com.calmwolfs.bedwar.utils.ChatUtils
import com.calmwolfs.bedwar.utils.HypixelUtils
import com.calmwolfs.bedwar.utils.MinecraftUtils
import com.calmwolfs.bedwar.utils.StringUtils.unformat
import com.calmwolfs.bedwar.utils.computer.ClipboardUtils
import com.calmwolfs.bedwar.utils.computer.KeyboardUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ChatLine
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.GuiNewChat
import net.minecraft.util.MathHelper
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Mouse

class CopyChat {
    private val config get() = BedWarMod.feature.chat
    private var lastChatX = 0
    private var lastChatY = 0
    private var hoveredChatLine: ChatLine? = null

    // A lot of code taken and edited from skytils
    private fun GuiNewChat.getChatLine(mouseX: Int, mouseY: Int): ChatLine? {
        if (chatOpen && this is AccessorGuiNewChat) {
            val scaleFactor = MinecraftUtils.scaleFactor()
            val fontHeight = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT

            val xPos = MathHelper.floor_float((mouseX / scaleFactor - lastChatX - 3).toFloat() / chatScale)
            val yPos = MathHelper.floor_float((mouseY / scaleFactor - (MinecraftUtils.scaledHeight() - lastChatY - 20)).toFloat() / chatScale)

            if (xPos >= 0 && yPos >= 0) {
                val line = this.lineCount.coerceAtMost(this.drawnChatLines.size)
                if (xPos <= MathHelper.floor_float(this.chatWidth.toFloat() / this.chatScale) && yPos < fontHeight * line + line) {
                    val lineNum = yPos / fontHeight + this.scrollPos
                    if (lineNum >= 0 && lineNum < this.drawnChatLines.size) {
                        return drawnChatLines[lineNum]
                    }
                }
            }
        }
        return null
    }

    // todo copy the whole message not just a single line of it
    @SubscribeEvent
    fun onAttemptCopy(event: GuiScreenEvent.MouseInputEvent.Pre) {
        if (!HypixelUtils.onHypixel || event.gui !is GuiChat || !Mouse.getEventButtonState()) return
        val chat = Minecraft.getMinecraft().ingameGUI.chatGUI
        chat as AccessorGuiNewChat

        if (config.copyChat) {
            if (Mouse.getEventButton() != 0) return
            if (!KeyboardUtils.isControlKeyDown()) return
            val chatLine = hoveredChatLine ?: return
            val chatText = chatLine.chatComponent.unformattedText.unformat()
            ClipboardUtils.copyToClipboard(chatText)
            ChatUtils.chat("§a[BedWar] §7Copied chat to clipboard!")
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onRenderChat(event: RenderGameOverlayEvent.Chat) {
        lastChatX = event.posX
        lastChatY = event.posY
    }

    @SubscribeEvent
    fun preDrawScreen(event: GuiScreenEvent.DrawScreenEvent.Pre) {
        val chat = Minecraft.getMinecraft().ingameGUI.chatGUI
        chat as AccessorGuiNewChat
        hoveredChatLine = if (config.copyChat && chat.chatOpen) chat.getChatLine(Mouse.getX(), Mouse.getY()) else null
    }
}